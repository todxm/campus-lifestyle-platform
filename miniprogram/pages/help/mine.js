const { BASE_URL } = require('../../utils/config')

Page({
  data: {
    activeTab: 'published',
    userContact: '',
    contactInput: '',
    tasks: [],
    isLoading: false
  },

  onShow() {
    const userContact = wx.getStorageSync('userContact') || ''
    this.setData({
      userContact,
      contactInput: userContact
    })

    if (userContact) {
      this.loadMyTasks()
    } else {
      this.setData({
        tasks: []
      })
    }
  },

  inputContact(event) {
    this.setData({
      contactInput: event.detail.value
    })
  },

  saveContact() {
    const contact = this.data.contactInput.trim()
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
    wx.showToast({
      title: '已保存',
      icon: 'success'
    })
    this.loadMyTasks()
  },

  changeTab(event) {
    this.setData({
      activeTab: event.currentTarget.dataset.tab
    })
    if (this.data.userContact) {
      this.loadMyTasks()
    }
  },

  // 开发阶段没有登录，先按本地联系方式筛选“我发布的”和“我接的”。
  loadMyTasks() {
    this.setData({ isLoading: true })

    wx.request({
      url: `${BASE_URL}/help-tasks`,
      method: 'GET',
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300 && Array.isArray(res.data)) {
          const filtered = res.data
            .filter(item => this.isMyTask(item))
            .map(item => this.formatTask(item))

          this.setData({
            tasks: filtered
          })
        } else {
          console.error('我的帮拿接口返回异常:', res.statusCode, res.data)
          this.setData({ tasks: [] })
          wx.showToast({
            title: '加载失败，请检查后端是否启动',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        this.setData({ tasks: [] })
        wx.showToast({
          title: '加载失败，请检查后端是否启动',
          icon: 'none'
        })
      },
      complete: () => {
        this.setData({ isLoading: false })
      }
    })
  },

  isMyTask(item) {
    if (this.data.activeTab === 'published') {
      return item.contact === this.data.userContact
    }
    return item.helperContact === this.data.userContact
  },

  formatTask(item) {
    const pickup = item.pickupLocation || '取件地点'
    const delivery = item.deliveryLocation || '送达地点'
    const rewardNumber = Number(item.reward || 0)

    return {
      id: item.id,
      type: item.taskType || '其他',
      route: `${pickup} → ${delivery}`,
      reward: `${rewardNumber.toFixed(0)}元`,
      status: item.status || '待接单',
      statusClass: this.getStatusClass(item.status),
      timeSlot: item.timeSlot || '饭点',
      remark: item.remark || '发布人暂时没有填写备注'
    }
  },

  getStatusClass(status) {
    if (status === '待接单') return 'waiting'
    if (status === '已接单') return 'taken'
    if (status === '已完成') return 'done'
    return 'cancelled'
  },

  openTaskDetail(event) {
    wx.navigateTo({
      url: `/pages/help/detail?id=${event.currentTarget.dataset.id}`
    })
  },

  goPublish() {
    wx.navigateTo({
      url: '/pages/help/publish'
    })
  }
})
