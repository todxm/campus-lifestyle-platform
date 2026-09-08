const { BASE_URL } = require('../../utils/config')

Page({
  data: {
    id: null,
    post: null,
    isLoading: false
  },

  onLoad(options) {
    this.setData({
      id: options.id
    })
    this.loadPostDetail()
  },

  // 根据列表传来的 id 读取帖子详情。
  loadPostDetail() {
    if (!this.data.id) {
      wx.showToast({
        title: '帖子不存在',
        icon: 'none'
      })
      return
    }

    this.setData({ isLoading: true })

    wx.request({
      url: `${BASE_URL}/wall-posts/${this.data.id}`,
      method: 'GET',
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300 && res.data) {
          this.setData({
            post: this.formatPost(res.data)
          })
        } else {
          console.error('校园墙详情接口返回异常:', res.statusCode, res.data)
          wx.showToast({
            title: '详情加载失败',
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

  formatPost(item) {
    const postType = item.postType || '校园动态'
    const status = item.status || '已发布'

    return {
      id: item.id,
      postType,
      typeClass: this.getTypeClass(postType),
      title: item.title || '未填写标题',
      content: item.content || '',
      location: item.location || '未填写',
      contact: item.contact || '未填写',
      tag: item.tag || this.getDefaultTag(postType),
      status,
      isClosed: status === '已关闭',
      createdTime: this.formatTime(item.createdTime)
    }
  },

  getTypeClass(postType) {
    if (postType === '失物招领') return 'lost'
    if (postType === '新生提问') return 'question'
    return 'news'
  },

  getDefaultTag(postType) {
    if (postType === '失物招领') return '待认领'
    if (postType === '新生提问') return '新生求助'
    return '校园提醒'
  },

  formatTime(value) {
    if (!value) return '未记录'
    return String(value).replace('T', ' ').slice(0, 16)
  }
})
