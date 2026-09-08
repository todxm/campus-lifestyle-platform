const { BASE_URL } = require('../../utils/config')

Page({
  data: {
    categories: ['宿舍用品', '教材资料', '数码配件', '生活用品', '其他'],
    urgencies: ['不急', '这两天', '今天就要'],
    categoryIndex: 0,
    urgencyIndex: 1,
    title: '',
    budgetMin: '',
    budgetMax: '',
    description: '',
    contact: '',
    isSubmitting: false
  },

  onLoad() {
    const userContact = wx.getStorageSync('userContact')
    if (userContact) {
      this.setData({
        contact: userContact
      })
    }
  },

  changeCategory(event) {
    this.setData({
      categoryIndex: Number(event.detail.value)
    })
  },

  changeUrgency(event) {
    this.setData({
      urgencyIndex: Number(event.detail.value)
    })
  },

  inputTitle(event) {
    this.setData({ title: event.detail.value })
  },

  inputBudgetMin(event) {
    this.setData({ budgetMin: event.detail.value })
  },

  inputBudgetMax(event) {
    this.setData({ budgetMax: event.detail.value })
  },

  inputDescription(event) {
    this.setData({ description: event.detail.value })
  },

  inputContact(event) {
    this.setData({ contact: event.detail.value })
  },

  validateForm() {
    const title = this.data.title.trim()
    const description = this.data.description.trim()
    const contact = this.data.contact.trim()
    const budgetMinText = this.data.budgetMin.trim()
    const budgetMaxText = this.data.budgetMax.trim()
    const budgetMin = budgetMinText === '' ? null : Number(budgetMinText)
    const budgetMax = budgetMaxText === '' ? null : Number(budgetMaxText)

    if (!title) return '请填写求购标题'
    if (!description) return '请填写需求说明'
    if (!contact) return '请填写联系方式'
    if (budgetMinText !== '' && (Number.isNaN(budgetMin) || budgetMin < 0)) return '最低预算不能小于 0'
    if (budgetMaxText !== '' && (Number.isNaN(budgetMax) || budgetMax < 0)) return '最高预算不能小于 0'
    if (budgetMin != null && budgetMax != null && budgetMax < budgetMin) return '最高预算不能小于最低预算'

    return ''
  },

  submitWanted() {
    if (this.data.isSubmitting) return

    const errorMessage = this.validateForm()
    if (errorMessage) {
      wx.showToast({
        title: errorMessage,
        icon: 'none'
      })
      return
    }

    const budgetMinText = this.data.budgetMin.trim()
    const budgetMaxText = this.data.budgetMax.trim()
    const requestBody = {
      title: this.data.title.trim(),
      category: this.data.categories[this.data.categoryIndex],
      budgetMin: budgetMinText === '' ? null : Number(budgetMinText),
      budgetMax: budgetMaxText === '' ? null : Number(budgetMaxText),
      description: this.data.description.trim(),
      contact: this.data.contact.trim(),
      urgency: this.data.urgencies[this.data.urgencyIndex]
    }

    this.setData({ isSubmitting: true })
    let published = false

    wx.request({
      url: `${BASE_URL}/wanted-items`,
      method: 'POST',
      data: requestBody,
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          published = true
          wx.setStorageSync('userContact', requestBody.contact)
          wx.setStorageSync('marketDefaultTab', 'wanted')
          wx.showToast({
            title: '发布成功',
            icon: 'success'
          })
          setTimeout(() => {
            const pages = getCurrentPages()
            if (pages.length > 1) {
              wx.navigateBack()
            } else {
              wx.switchTab({
                url: '/pages/market/index'
              })
            }
          }, 600)
        } else {
          console.error('发布求购接口返回异常:', res.statusCode, res.data)
          wx.showToast({
            title: '发布失败，请检查信息',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        wx.showToast({
          title: '发布失败，请检查后端',
          icon: 'none'
        })
      },
      complete: () => {
        if (!published) this.setData({ isSubmitting: false })
      }
    })
  }
})
