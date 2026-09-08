const { BASE_URL } = require('../../utils/config')

Page({
  data: {
    activeChannel: '全部',
    channels: ['全部', '失物招领', '新生提问', '校园动态'],
    posts: [],
    isLoading: false
  },

  onShow() {
    this.loadWallPosts()
  },

  changeChannel(event) {
    const channel = event.currentTarget.dataset.channel
    this.setData({
      activeChannel: channel
    })
    this.loadWallPosts(channel)
  },

  // 从后端读取校园墙帖子。选择“全部”时不传 postType。
  loadWallPosts(channel = this.data.activeChannel) {
    const requestData = channel === '全部' ? {} : { postType: channel }
    // 只允许最新请求更新列表、错误提示和加载状态。
    const requestId = (this.wallRequestId || 0) + 1
    this.wallRequestId = requestId

    this.setData({ isLoading: true })

    wx.request({
      url: `${BASE_URL}/wall-posts`,
      method: 'GET',
      data: requestData,
      success: (res) => {
        if (requestId !== this.wallRequestId) return
        if (res.statusCode >= 200 && res.statusCode < 300 && Array.isArray(res.data)) {
          this.setData({
            posts: res.data.map(item => this.formatPost(item))
          })
        } else {
          console.error('校园墙接口返回异常:', res.statusCode, res.data)
          this.setData({ posts: [] })
          wx.showToast({
            title: '加载失败，请检查后端是否启动',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        if (requestId !== this.wallRequestId) return
        console.error('请求失败:', err)
        this.setData({ posts: [] })
        wx.showToast({
          title: '加载失败，请检查后端是否启动',
          icon: 'none'
        })
      },
      complete: () => {
        if (requestId !== this.wallRequestId) return
        this.setData({ isLoading: false })
      }
    })
  },

  // 整理后端字段，WXML 里就不用写复杂判断。
  formatPost(item) {
    return {
      id: item.id,
      postType: item.postType || '校园动态',
      typeClass: this.getTypeClass(item.postType),
      title: item.title || '未填写标题',
      content: item.content || '',
      location: item.location || '',
      contact: item.contact || '',
      tag: item.tag || this.getDefaultTag(item.postType),
      status: item.status || '已发布',
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
    if (!value) return '刚刚'
    return String(value).replace('T', ' ').slice(0, 16)
  },

  publishPost() {
    wx.navigateTo({
      url: '/pages/wall/publish'
    })
  },

  openPostDetail(event) {
    wx.navigateTo({
      url: `/pages/wall/detail?id=${event.currentTarget.dataset.id}`
    })
  }
})
