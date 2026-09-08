const { BASE_URL } = require('../../utils/config')

Page({
  data: {
    categories: ['全部', '驾校', '校园卡宽带', '打印复印', '兼职信息', '家教辅导', '生活服务', '其他'],
    activeCategory: '全部',
    serviceItems: [],
    displayServices: [],
    isLoading: false
  },

  onShow() {
    this.loadServices()
  },

  // 从后端读取校园服务列表；后端沿用已有 /service-items 接口。
  loadServices() {
    this.setData({ isLoading: true })
    wx.request({
      url: `${BASE_URL}/service-items`,
      method: 'GET',
      success: (res) => {
        if (res.statusCode === 200 && Array.isArray(res.data)) {
          const serviceItems = res.data.map((item) => this.formatService(item))
          this.setData({ serviceItems, isLoading: false })
          this.applyCategoryFilter()
        } else {
          this.setData({ isLoading: false })
          wx.showToast({ title: '服务加载失败', icon: 'none' })
        }
      },
      fail: (err) => {
        console.error('服务榜请求失败:', err)
        this.setData({ isLoading: false })
        wx.showToast({ title: '加载失败，请检查后端', icon: 'none' })
      }
    })
  },

  // 统一整理后端字段，方便 WXML 直接展示。
  formatService(item) {
    const categoryLabel = item.serviceCategory || item.type || '校园服务'
    const contactCount = item.contactClickCount || 0
    const desc = item.description || '这个服务暂时还没有填写详细介绍。'

    return {
      ...item,
      categoryLabel,
      contactCount,
      shortDescription: desc.length > 42 ? `${desc.slice(0, 42)}...` : desc,
      contactModeText: this.getContactModeText(item.contactMode),
      isFeaturedFlag: item.isFeatured === 1,
      isVerifiedFlag: item.isVerified === 1
    }
  },

  getContactModeText(contactMode) {
    if (contactMode === 'PLATFORM') {
      return '平台顾问中转'
    }
    if (contactMode === 'CODE') {
      return '联系时带暗号'
    }
    return '直接联系'
  },

  changeCategory(event) {
    const activeCategory = event.currentTarget.dataset.category
    this.setData({ activeCategory })
    this.applyCategoryFilter()
  },

  // 小程序端做轻量分类筛选，避免为了展示页重复新增后端服务表。
  applyCategoryFilter() {
    const { activeCategory, serviceItems } = this.data
    const displayServices = activeCategory === '全部'
      ? serviceItems
      : serviceItems.filter((item) => this.matchCategory(item, activeCategory))

    this.setData({ displayServices })
  },

  matchCategory(item, activeCategory) {
    const keywordsMap = {
      '驾校': ['驾校', '考证'],
      '校园卡宽带': ['校园卡', '宽带', '电话卡'],
      '打印复印': ['打印', '复印'],
      '兼职信息': ['兼职', '招聘'],
      '家教辅导': ['家教', '培训', '辅导'],
      '生活服务': ['生活', '跑腿', '零食', '维修'],
      '其他': ['其他']
    }
    const keywords = keywordsMap[activeCategory] || []
    const text = [item.serviceCategory, item.type, item.name, item.description, item.promotionLabel]
      .filter(Boolean)
      .join(' ')
    return keywords.some((keyword) => text.includes(keyword))
  },

  openServiceDetail(event) {
    const id = event.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/service/detail?id=${id}`
    })
  }
})
