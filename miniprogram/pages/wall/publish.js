const { BASE_URL } = require('../../utils/config')

Page({
  data: {
    postTypes: ['失物招领', '新生提问', '校园动态'],
    typeIndex: 0,
    title: '',
    content: '',
    location: '',
    contact: '',
    tag: '',
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

  inputTitle(event) {
    this.setData({ title: event.detail.value })
  },

  inputContent(event) {
    this.setData({ content: event.detail.value })
  },

  inputLocation(event) {
    this.setData({ location: event.detail.value })
  },

  inputContact(event) {
    this.setData({ contact: event.detail.value })
  },

  inputTag(event) {
    this.setData({ tag: event.detail.value })
  },

  submitPost() {
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
    let published = false

    wx.request({
      url: `${BASE_URL}/wall-posts`,
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      data: payload,
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          published = true
          wx.showToast({
            title: '发布成功',
            icon: 'success'
          })
          setTimeout(() => {
            wx.navigateBack()
          }, 700)
        } else {
          console.error('校园墙发布接口返回异常:', res.statusCode, res.data)
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
        // 成功后保持锁定，避免返回页面前再次提交同一条内容。
        if (!published) {
          this.setData({ isSubmitting: false })
        }
      }
    })
  },

  buildPayload() {
    return {
      postType: this.data.postTypes[this.data.typeIndex],
      title: this.data.title.trim(),
      content: this.data.content.trim(),
      location: this.data.location.trim(),
      contact: this.data.contact.trim(),
      tag: this.data.tag.trim()
    }
  },

  validatePayload(payload) {
    if (!payload.postType) return '请选择帖子类型'
    if (!payload.title) return '请填写标题'
    if (!payload.content) return '请填写内容'
    return ''
  }
})
