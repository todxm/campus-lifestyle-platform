Page({
  data: {
    quickTags: [
      { text: '饭点帮拿', color: 'pink', icon: '🍱' },
      { text: '失物招领', color: 'blue', icon: '📌' },
      { text: '求购墙', color: 'orange', icon: '🛍️' },
      { text: '校园服务', color: 'green', icon: '🛡️' },
      { text: '今日捡漏', color: 'purple', icon: '💎' }
    ],
    intelCards: [
      { icon: '📣', title: '今日情报', desc: '上海路雷达开机' },
      { icon: '🍱', title: '最新帮拿', desc: '校门口 → 3栋302' },
      { icon: '📝', title: '新增求购', desc: '蹲一个床上桌' },
      { icon: '📌', title: '最新失物', desc: '捡到校园卡' }
    ],
    entries: [
      {
        title: '饭点帮拿',
        desc: '下课顺手带一趟',
        button: '去发布',
        color: 'food',
        icon: '🍱',
        action: 'goHelpPublish'
      },
      {
        title: '上海路蹲一个',
        desc: '想要什么，先挂出来',
        button: '去求购',
        color: 'wish',
        icon: '📝',
        action: 'goMarketWanted'
      },
      {
        title: '校园墙',
        desc: '看看今天上海路发生了什么',
        button: '去看看',
        color: 'wall',
        icon: '📣',
        action: 'goWall'
      },
      {
        title: '靠谱服务榜',
        desc: '别乱加人，先看学长整理',
        button: '去看看',
        color: 'service',
        icon: '🛡️',
        action: 'goService'
      }
    ],
    hotItems: [
      { title: '床上桌', price: '¥20', tag: '热门', color: 'hot' },
      { title: '软件工程教材', price: '¥35', tag: '新生刚需', color: 'need' },
      { title: '驾校咨询', price: '推荐', tag: '避坑先看', color: 'safe' },
      { title: '打印店', price: '附近', tag: '新生刚需', color: 'print' }
    ]
  },

  goHelpPublish() {
    wx.navigateTo({
      url: '/pages/help/publish'
    })
  },

  goHelp() {
    wx.switchTab({
      url: '/pages/help/index'
    })
  },

  goWall() {
    wx.switchTab({
      url: '/pages/wall/index'
    })
  },

  goMarket() {
    wx.switchTab({
      url: '/pages/market/index'
    })
  },

  goMarketWanted() {
    wx.setStorageSync('marketDefaultTab', 'wanted')
    wx.switchTab({
      url: '/pages/market/index'
    })
  },


  goService() {
    wx.navigateTo({
      url: '/pages/service/index'
    })
  },

  showComingSoon() {
    wx.showToast({
      title: '这一块后续开放',
      icon: 'none'
    })
  },

  tapHotItem() {
    wx.showToast({
      title: '详情后续开放',
      icon: 'none'
    })
  },

  handleEntryTap(event) {
    const action = event.currentTarget.dataset.action
    if (action && this[action]) {
      this[action]()
    }
  }
})
