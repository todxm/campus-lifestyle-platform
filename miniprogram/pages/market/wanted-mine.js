const { BASE_URL } = require('../../utils/config')

Page({
  data: {
    userContact: '',
    inputContact: '',
    wantedItems: [],
    isLoading: false
  },

  onShow() {
    const userContact = wx.getStorageSync('userContact') || ''
    this.setData({
      userContact,
      inputContact: userContact
    })
    if (userContact) {
      this.loadMyWantedItems(userContact)
    }
  },

  inputContactValue(event) {
    this.setData({
      inputContact: event.detail.value
    })
  },

  saveContact() {
    const contact = this.data.inputContact.trim()
    if (!contact) {
      wx.showToast({
        title: '请先填写联系方式',
        icon: 'none'
      })
      return
    }
    wx.setStorageSync('userContact', contact)
    this.setData({
      userContact: contact
    })
    this.loadMyWantedItems(contact)
  },

  loadMyWantedItems(contact = this.data.userContact) {
    const requestId = (this.wantedRequestId || 0) + 1
    this.wantedRequestId = requestId
    this.setData({ isLoading: true })

    wx.request({
      url: `${BASE_URL}/wanted-items`,
      method: 'GET',
      success: (res) => {
        if (requestId !== this.wantedRequestId) return
        if (res.statusCode >= 200 && res.statusCode < 300 && Array.isArray(res.data)) {
          this.setData({
            wantedItems: res.data
              .filter(item => item.contact === contact)
              .map(item => this.formatWantedItem(item))
          })
        } else {
          console.error('我的求购接口返回异常:', res.statusCode, res.data)
          wx.showToast({
            title: '加载失败',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        if (requestId !== this.wantedRequestId) return
        console.error('请求失败:', err)
        wx.showToast({
          title: '加载失败，请检查后端',
          icon: 'none'
        })
      },
      complete: () => {
        if (requestId !== this.wantedRequestId) return
        this.setData({ isLoading: false })
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

  openWantedDetail(event) {
    wx.navigateTo({
      url: `/pages/market/wanted-detail?id=${event.currentTarget.dataset.id}`
    })
  }
})
