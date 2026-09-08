const { BASE_URL } = require('../../utils/config')

Page({
  data: {
    id: null,
    service: null,
    isLoading: false,
    contactVisible: false,
    contactText: '',
    contactButtonText: '查看联系方式'
  },

  onLoad(options) {
    this.setData({ id: options.id })
    this.loadServiceDetail()
  },

  // 根据 id 读取服务详情。
  loadServiceDetail() {
    const { id } = this.data
    if (!id) {
      wx.showToast({ title: '服务不存在', icon: 'none' })
      return
    }

    this.setData({ isLoading: true })
    wx.request({
      url: `${BASE_URL}/service-items/${id}`,
      method: 'GET',
      success: (res) => {
        if (res.statusCode === 200 && res.data) {
          const service = this.formatService(res.data)
          this.setData({
            service,
            isLoading: false,
            contactText: this.getContactText(service),
            contactButtonText: this.getContactButtonText(service)
          })
        } else {
          this.setData({ isLoading: false })
          wx.showToast({ title: '服务详情加载失败', icon: 'none' })
        }
      },
      fail: (err) => {
        console.error('服务详情请求失败:', err)
        this.setData({ isLoading: false })
        wx.showToast({ title: '加载失败，请检查后端', icon: 'none' })
      }
    })
  },

  formatService(item) {
    return {
      ...item,
      categoryLabel: item.serviceCategory || item.type || '校园服务',
      contactCount: item.contactClickCount || 0,
      contactModeText: this.getContactModeText(item.contactMode),
      isFeaturedFlag: item.isFeatured === 1,
      isVerifiedFlag: item.isVerified === 1
    }
  },

  getContactModeText(contactMode) {
    if (contactMode === 'PLATFORM') {
      return '平台商业号中转'
    }
    if (contactMode === 'CODE') {
      return '联系商家时带暗号'
    }
    return '直接联系商家'
  },

  getContactButtonText(service) {
    if (service.contactMode === 'PLATFORM') {
      return '联系平台顾问'
    }
    if (service.contactMode === 'CODE') {
      return '复制商家微信'
    }
    return '查看联系方式'
  },

  getContactText(service) {
    if (service.contactMode === 'PLATFORM') {
      return service.platformContact || service.contactWechat || ''
    }
    return service.contactWechat || ''
  },

  // 点击联系时先记录一次 contact-click，再展示或复制联系方式。
  showContact() {
    const { id, service, contactText } = this.data
    if (!service) {
      return
    }

    wx.request({
      url: `${BASE_URL}/service-items/${id}/contact-click`,
      method: 'PUT',
      success: (res) => {
      },
      fail: (err) => {
        console.error('联系点击统计失败:', err)
      }
    })

    if (!contactText) {
      this.setData({ contactVisible: true })
      wx.showToast({ title: '暂未公开联系方式', icon: 'none' })
      return
    }

    this.setData({
      contactVisible: true,
      service: {
        ...service,
        contactCount: service.contactCount + 1
      }
    })

    wx.setClipboardData({
      data: contactText,
      success: () => {
        const title = service.contactMode === 'CODE' && service.contactCode
          ? `已复制，咨询时说${service.contactCode}`
          : '联系方式已复制'
        wx.showToast({ title, icon: 'none' })
      }
    })
  }
})
