const { BASE_URL } = require('../../utils/config')

Page({
  data: {
    activeFilter: '全部',
    filters: ['全部', '外卖代拿', '快递代取', '打印资料', '其他'],
    filteredTasks: [],
    isLoading: false
  },

  onShow() {
    this.loadHelpTasks()
  },

  changeFilter(event) {
    const filter = event.currentTarget.dataset.filter
    this.setData({
      activeFilter: filter
    })
    this.loadHelpTasks(filter)
  },

  // 从后端读取帮拿任务列表。选择“全部”时不传 taskType。
  loadHelpTasks(filter = this.data.activeFilter) {
    const requestData = filter === '全部' ? {} : { taskType: filter }

    this.setData({ isLoading: true })

    wx.request({
      url: `${BASE_URL}/help-tasks`,
      method: 'GET',
      data: requestData,
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300 && Array.isArray(res.data)) {
          this.setData({
            filteredTasks: res.data.map(item => this.formatTask(item))
          })
        } else {
          console.error('帮拿任务接口返回异常:', res.statusCode, res.data)
          this.setData({
            filteredTasks: []
          })
          wx.showToast({
            title: '加载失败，请检查后端是否启动',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        this.setData({
          filteredTasks: []
        })
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

  // 把后端字段转换成页面更容易展示的字段。
  formatTask(item) {
    const status = item.status || '待接单'
    const pickup = item.pickupLocation || '取件地点'
    const delivery = item.deliveryLocation || '送达地点'
    const rewardNumber = Number(item.reward || 0)

    return {
      id: item.id,
      type: item.taskType || '其他',
      route: `${pickup} → ${delivery}`,
      detailLocation: item.detailLocation || '',
      reward: `${rewardNumber.toFixed(0)}元`,
      deadline: item.timeSlot ? `${item.timeSlot}饭点` : '饭点帮拿',
      contact: item.contact || '',
      helperContact: item.helperContact || '',
      status,
      statusClass: this.getStatusClass(status),
      remark: item.remark || '发布人暂时没有填写备注',
      labels: this.buildLabels(item),
      actionText: this.getActionText(status)
    }
  },

  getStatusClass(status) {
    if (status === '待接单') return 'waiting'
    if (status === '已接单') return 'taken'
    return 'done'
  },

  getActionText(status) {
    if (status === '待接单') return '我来帮'
    if (status === '已接单') return '已有人接'
    if (status === '已取消') return '已取消'
    return '已完成'
  },

  buildLabels(item) {
    const labels = []
    if (item.timeSlot) labels.push(item.timeSlot)
    if (item.contact) labels.push(`联系：${item.contact}`)
    if (item.helperContact) labels.push(`互助员：${item.helperContact}`)
    return labels.length > 0 ? labels : ['饭点帮拿']
  },

  helpTask(event) {
    const id = event.currentTarget.dataset.id
    const status = event.currentTarget.dataset.status

    if (status !== '待接单') {
      wx.showToast({
        title: '该任务已不可接单',
        icon: 'none'
      })
      return
    }

    wx.request({
      url: `${BASE_URL}/help-tasks/${id}/accept`,
      method: 'PUT',
      header: {
        'content-type': 'application/json'
      },
      data: {
        helperContact: this.getLocalContact()
      },
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          wx.showToast({
            title: '接单成功',
            icon: 'success'
          })
          this.loadHelpTasks(this.data.activeFilter)
        } else {
          console.error('接单接口返回异常:', res.statusCode, res.data)
          wx.showToast({
            title: '该任务已不可接单',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        wx.showToast({
          title: '接单失败，请检查后端是否启动',
          icon: 'none'
        })
      }
    })
  },

  getLocalContact() {
    return wx.getStorageSync('userContact') || 'helper001'
  },

  showHelperTip() {
    wx.showToast({
      title: '先在任务卡片里点“我来帮”',
      icon: 'none'
    })
  },

  goPublish() {
    wx.navigateTo({
      url: '/pages/help/publish'
    })
  },

  goMine() {
    wx.navigateTo({
      url: '/pages/help/mine'
    })
  },

  openTaskDetail(event) {
    wx.navigateTo({
      url: `/pages/help/detail?id=${event.currentTarget.dataset.id}`
    })
  }
})
