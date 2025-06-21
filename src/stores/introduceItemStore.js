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
    async addItem({ title, memberId, introduceTemplateId }) {
      const newItem = await createItem({ title, memberId, introduceTemplateId })
      this.items.push(newItem)
    },
    async removeItem(id) {
      await deleteItem(id)
      this.items = this.items.filter(item => item.id !== id)
    }
  }
})
