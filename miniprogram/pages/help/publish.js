const { BASE_URL } = require('../../utils/config')

Page({
  data: {
    types: ['外卖代拿', '快递代取', '打印资料', '其他'],
    typeIndex: 0,
    pickupPlaces: ['校门口', '菜鸟驿站', '打印店', '其他'],
    pickupIndex: 0,
    deliveryPlaces: ['宿舍楼', '教学楼', '其他'],
    deliveryIndex: 0,
    times: ['中午', '晚上'],
    timeIndex: 0,
    reward: 2,
    detailLocation: '',
    contact: '',
    remark: '',
    isSubmitting: false
  },

  onLoad() {
    const userContact = wx.getStorageSync('userContact') || ''
    if (userContact) {
      this.setData({
        contact: userContact
      })
    }
  },

  changeType(event) {
    this.setData({
      typeIndex: Number(event.detail.value)
    })
  },

  changePickup(event) {
    this.setData({
      pickupIndex: Number(event.detail.value)
    })
  },

  changeDelivery(event) {
    this.setData({
      deliveryIndex: Number(event.detail.value)
    })
  },

  changeTime(event) {
    this.setData({
      timeIndex: Number(event.detail.value)
    })
  },

  minusReward() {
    const nextReward = Math.max(0, this.data.reward - 1)
    this.setData({ reward: nextReward })
  },

  plusReward() {
    this.setData({
      reward: this.data.reward + 1
    })
  },

  inputDetailLocation(event) {
    this.setData({
      detailLocation: event.detail.value
    })
  },

  inputContact(event) {
    this.setData({
      contact: event.detail.value
    })
  },

  inputRemark(event) {
    this.setData({
      remark: event.detail.value
    })
  },

  submitTask() {
    if (this.data.isSubmitting) {
      return
    }

    const payload = this.buildPayload()
    const errorMessage = this.validatePayload(payload)

    if (errorMessage) {
      wx.showToast({
        title: errorMessage,
        icon: 'none'
      })
      return
    }

    this.setData({ isSubmitting: true })

    wx.request({
      url: `${BASE_URL}/help-tasks`,
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      data: payload,
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          wx.setStorageSync('userContact', payload.contact)
          wx.showToast({
            title: '发布成功',
            icon: 'success'
          })
          setTimeout(() => {
            wx.navigateBack()
          }, 700)
        } else {
          console.error('发布接口返回异常:', res.statusCode, res.data)
          wx.showToast({
            title: '发布失败，请检查信息',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        wx.showToast({
          title: '发布失败，请检查后端是否启动',
          icon: 'none'
        })
      },
      complete: () => {
        this.setData({ isSubmitting: false })
      }
    })
  },

  buildPayload() {
    return {
      taskType: this.data.types[this.data.typeIndex],
      pickupLocation: this.data.pickupPlaces[this.data.pickupIndex],
      deliveryLocation: this.data.deliveryPlaces[this.data.deliveryIndex],
      detailLocation: this.data.detailLocation.trim(),
      timeSlot: this.data.times[this.data.timeIndex],
      reward: Number(this.data.reward),
      contact: this.data.contact.trim(),
      remark: this.data.remark.trim()
    }
  },

  validatePayload(payload) {
    if (!payload.taskType) return '请选择任务类型'
    if (!payload.pickupLocation) return '请选择取件地点'
    if (!payload.deliveryLocation) return '请选择送达地点'
    if (!payload.timeSlot) return '请选择时间段'
    if (Number.isNaN(payload.reward) || payload.reward < 0) return '酬劳不能小于 0'
    if (!payload.contact) return '请填写联系方式'
    return ''
  }
})
