const { BASE_URL } = require('../../utils/config')

Page({
  data: {
    id: null,
    wanted: null,
    isLoading: false
  },

  onLoad(options) {
    this.setData({
      id: options.id
    })
    this.loadWantedDetail()
  },

  loadWantedDetail() {
    if (!this.data.id) return

    this.setData({ isLoading: true })

    wx.request({
      url: `${BASE_URL}/wanted-items/${this.data.id}`,
      method: 'GET',
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300 && res.data) {
          this.setData({
            wanted: this.formatWantedItem(res.data)
          })
        } else {
          console.error('求购详情接口返回异常:', res.statusCode, res.data)
          wx.showToast({
            title: '加载失败',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        wx.showToast({
          title: '加载失败，请检查后端',
          icon: 'none'
        })
      },
      complete: () => {
        this.setData({ isLoading: false })
      }
    })
  },

  formatWantedItem(item) {
    return {
      id: item.id,
      title: item.title || '未填写求购标题',
      category: item.category || '其他',
      budgetText: this.formatBudget(item.budgetMin, item.budgetMax),
      description: item.description || '',
      contact: item.contact || '',
      urgency: item.urgency || '不急',
      status: item.status || '求购中',
      createdTime: this.formatTime(item.createdTime)
    }
  },

  formatBudget(min, max) {
    if (min != null && max != null) return `${min}-${max} 元`
    if (min != null) return `${min} 元起`
    if (max != null) return `${max} 元以内`
    return '预算可商量'
  },

  formatTime(value) {
    if (!value) return '刚刚'
    return String(value).replace('T', ' ').slice(0, 16)
  },

  showContactTip() {
    wx.showToast({
      title: '后续接入联系功能',
      icon: 'none'
    })
  },

  markFound() {
    this.updateStatus('found', '已标记为已找到')
  },

  closeWanted() {
    wx.showModal({
      title: '关闭求购',
      content: '确定关闭这个求购吗？关闭后会显示为已关闭。',
      success: (res) => {
        if (res.confirm) {
          this.updateStatus('close', '求购已关闭')
        }
      }
    })
  },

  updateStatus(action, successTitle) {
    wx.request({
      url: `${BASE_URL}/wanted-items/${this.data.id}/${action}`,
      method: 'PUT',
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          wx.showToast({
            title: successTitle,
            icon: 'success'
          })
          this.loadWantedDetail()
        } else {
          console.error('求购状态接口返回异常:', res.statusCode, res.data)
          wx.showToast({
            title: '操作失败',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        wx.showToast({
          title: '操作失败，请检查后端',
          icon: 'none'
        })
      }
    })
  }
})
