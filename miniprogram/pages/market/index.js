const { BASE_URL } = require('../../utils/config')

Page({
  data: {
    activeTab: '正在出售',
    tabs: ['正在出售', '求购墙'],
    categories: ['全部', '宿舍用品', '教材资料', '数码配件', '生活用品', '其他'],
    activeCategory: '全部',
    isLoadingWanted: false,
    saleItems: [
      { name: '床上桌', price: '20元', category: '宿舍用品', note: '能折叠，宿舍自用' },
      { name: '高数教材', price: '15元', category: '教材资料', note: '有少量笔记' },
      { name: '宿舍小风扇', price: '25元', category: '生活用品', note: '风力正常，可试' }
    ],
    wantedItems: []
  },

  onLoad() {
    this.applyWantedDefault()
  },

  onShow() {
    this.applyWantedDefault()
    if (this.data.activeTab === '求购墙') {
      this.loadWantedItems()
    }
  },

  // 首页“上海路蹲一个”入口会写入这个本地标记，集市页打开后自动切到求购墙。
  applyWantedDefault() {
    const defaultTab = wx.getStorageSync('marketDefaultTab')
    if (defaultTab === 'wanted') {
      wx.removeStorageSync('marketDefaultTab')
      this.setData({
        activeTab: '求购墙'
      })
    }
  },

  changeTab(event) {
    const tab = event.currentTarget.dataset.tab
    this.setData({
      activeTab: tab
    })
    if (tab === '求购墙') {
      this.loadWantedItems()
    }
  },

  changeCategory(event) {
    const category = event.currentTarget.dataset.category
    this.setData({
      activeCategory: category
    })
    if (this.data.activeTab === '求购墙') {
      this.loadWantedItems(category)
    }
  },

  // 从后端读取真实求购列表。选择“全部”时不传 category。
  loadWantedItems(category = this.data.activeCategory) {
    const requestId = (this.wantedRequestId || 0) + 1
    this.wantedRequestId = requestId
    const requestData = category === '全部' ? {} : { category }

    this.setData({ isLoadingWanted: true })

    wx.request({
      url: `${BASE_URL}/wanted-items`,
      method: 'GET',
      data: requestData,
      success: (res) => {
        if (requestId !== this.wantedRequestId) return
        if (res.statusCode >= 200 && res.statusCode < 300 && Array.isArray(res.data)) {
          this.setData({
            wantedItems: res.data.map(item => this.formatWantedItem(item))
          })
        } else {
          console.error('求购墙接口返回异常:', res.statusCode, res.data)
          this.setData({ wantedItems: [] })
          wx.showToast({
            title: '加载失败，请检查后端是否启动',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        if (requestId !== this.wantedRequestId) return
        console.error('请求失败:', err)
        this.setData({ wantedItems: [] })
        wx.showToast({
          title: '加载失败，请检查后端是否启动',
          icon: 'none'
        })
      },
      complete: () => {
        if (requestId !== this.wantedRequestId) return
        this.setData({ isLoadingWanted: false })
      }
    })
  },

  formatWantedItem(item) {
    return {
      id: item.id,
      title: item.title || '未填写求购标题',
      category: item.category || '其他',
      budgetText: this.formatBudget(item.budgetMin, item.budgetMax),
      description: item.description || '',
      contact: item.contact || '',
      urgency: item.urgency || '不急',
      status: item.status || '求购中',
      createdTime: this.formatTime(item.createdTime)
    }
  },

  formatBudget(min, max) {
    if (min != null && max != null) return `${min}-${max} 元`
    if (min != null) return `${min} 元起`
    if (max != null) return `${max} 元以内`
    return '预算可商量'
  },

  formatTime(value) {
    if (!value) return '刚刚'
    return String(value).replace('T', ' ').slice(0, 16)
  },

  publishWanted() {
    wx.navigateTo({
      url: '/pages/market/wanted-publish'
    })
  },

  openWantedDetail(event) {
    wx.navigateTo({
      url: `/pages/market/wanted-detail?id=${event.currentTarget.dataset.id}`
    })
  }
})
