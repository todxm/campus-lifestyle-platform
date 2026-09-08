Page({
  data: {
    menuItems: [
      { name: '我的帮拿任务', action: 'goHelpMine' },
      { name: '我的发布', action: 'showComingSoon' },
      { name: '我的求购', action: 'goWantedMine' },
      { name: '服务榜 / 商家合作', action: 'goService' },
      { name: '我的失物招领', action: 'showComingSoon' },
      { name: '联系平台', action: 'showComingSoon' },
      { name: '反馈建议', action: 'showComingSoon' }
    ]
  },

  tapMenu(event) {
    const action = event.currentTarget.dataset.action
    if (action && this[action]) {
      this[action](event.currentTarget.dataset.name)
    }
  },

  goHelpMine() {
    wx.navigateTo({
      url: '/pages/help/mine'
    })
  },

  goWantedMine() {
    wx.navigateTo({
      url: '/pages/market/wanted-mine'
    })
  },

  goService() {
    wx.navigateTo({
      url: '/pages/service/index'
    })
  },

  showComingSoon(name) {
    wx.showToast({
      title: name + ' 后续开放',
      icon: 'none'
    })
  }
})
