const { BASE_URL } = require('../../utils/config')

Page({
  data: {
    id: null,
    task: null,
    isLoading: false
  },

  onLoad(options) {
    this.setData({
      id: options.id
    })
    this.loadTaskDetail()
  },

  // 从后端读取单个帮拿任务详情。
  loadTaskDetail() {
    if (!this.data.id) {
      wx.showToast({
        title: '任务不存在',
        icon: 'none'
      })
      return
    }

    this.setData({ isLoading: true })

    wx.request({
      url: `${BASE_URL}/help-tasks/${this.data.id}`,
      method: 'GET',
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300 && res.data) {
          this.setData({
            task: this.formatTask(res.data)
          })
        } else {
          console.error('任务详情接口返回异常:', res.statusCode, res.data)
          wx.showToast({
            title: '任务详情加载失败',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
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

  // 把后端字段整理成页面展示字段，避免 WXML 里写复杂判断。
  formatTask(item) {
    const status = item.status || '待接单'
    const rewardNumber = Number(item.reward || 0)

    return {
      id: item.id,
      taskType: item.taskType || '其他',
      pickupLocation: item.pickupLocation || '未填写',
      deliveryLocation: item.deliveryLocation || '未填写',
      detailLocation: item.detailLocation || '未填写',
      timeSlot: item.timeSlot || '未填写',
      reward: `${rewardNumber.toFixed(0)} 元`,
      contact: item.contact || '未填写',
      remark: item.remark || '发布人暂时没有填写备注',
      status,
      statusClass: this.getStatusClass(status),
      helperContact: item.helperContact || '暂无',
      createdTime: this.formatTime(item.createdTime),
      canAccept: status === '待接单',
      canFinish: status === '已接单',
      canCancel: status === '待接单' || status === '已接单',
      isClosed: status === '已完成' || status === '已取消'
    }
  },

  getStatusClass(status) {
    if (status === '待接单') return 'waiting'
    if (status === '已接单') return 'taken'
    if (status === '已完成') return 'done'
    return 'cancelled'
  },

  formatTime(value) {
    if (!value) return '未记录'
    return String(value).replace('T', ' ')
  },

  getLocalContact() {
    return wx.getStorageSync('userContact') || 'helper001'
  },

  acceptTask() {
    wx.request({
      url: `${BASE_URL}/help-tasks/${this.data.id}/accept`,
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
          this.loadTaskDetail()
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
          title: '接单失败，请稍后再试',
          icon: 'none'
        })
      }
    })
  },

  finishTask() {
    wx.request({
      url: `${BASE_URL}/help-tasks/${this.data.id}/finish`,
      method: 'PUT',
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          wx.showToast({
            title: '任务已完成',
            icon: 'success'
          })
          this.loadTaskDetail()
        } else {
          console.error('完成任务接口返回异常:', res.statusCode, res.data)
          wx.showToast({
            title: '完成失败，请稍后再试',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        wx.showToast({
          title: '完成失败，请稍后再试',
          icon: 'none'
        })
      }
    })
  },

  cancelTask() {
    wx.showModal({
      title: '取消任务',
      content: '确定取消这个帮拿任务吗？',
      confirmText: '确定取消',
      confirmColor: '#d85b38',
      success: (modalRes) => {
        if (!modalRes.confirm) return

        wx.request({
          url: `${BASE_URL}/help-tasks/${this.data.id}/cancel`,
          method: 'PUT',
          success: (res) => {
            if (res.statusCode >= 200 && res.statusCode < 300) {
              wx.showToast({
                title: '任务已取消',
                icon: 'success'
              })
              this.loadTaskDetail()
            } else {
              console.error('取消任务接口返回异常:', res.statusCode, res.data)
              wx.showToast({
                title: '取消失败，请稍后再试',
                icon: 'none'
              })
            }
          },
          fail: (err) => {
            console.error('请求失败:', err)
            wx.showToast({
              title: '取消失败，请稍后再试',
              icon: 'none'
            })
          }
        })
      }
    })
  }
})
