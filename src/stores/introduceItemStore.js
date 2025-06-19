import { defineStore } from 'pinia'
import { fetchItems, createItem, deleteItem } from '@/services/introduceTemplateItemService'

export const useIntroduceItemStore = defineStore('introduceItem', {
  state: () => ({
    items: []
  }),
  actions: {
    async loadItems() {
      this.items = await fetchItems()
    },
    async addItem(content) {
      const memberId = 1
      const newItem = await createItem(content, memberId)
      this.items.push(newItem)
    },
    async removeItem(id) {
      await deleteItem(id)
      this.items = this.items.filter(item => item.id !== id)
    }
  }
})
