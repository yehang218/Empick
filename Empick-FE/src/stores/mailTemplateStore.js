import { defineStore } from 'pinia';
import {
  createTemplateService,
  updateTemplateService,
  deleteTemplateService,
  findAllTemplatesService,
  findTemplateByIdService,
  searchTemplateByTitleService,
} from '@/services/mailTemplateService';

export const useMailTemplateStore = defineStore('mailTemplate', {
  state: () => ({
    templates: [],
    currentTemplate: null,
    loading: false,
    error: null,
  }),

  actions: {
    async createTemplate(dto) {
      this.loading = true;
      try {
        const result = await createTemplateService(dto);
        return result;
      } catch (e) {
        this.error = e.message;
        throw e;
      } finally {
        this.loading = false;
      }
    },

    async updateTemplate(id, dto) {
      this.loading = true;
      try {
        const result = await updateTemplateService(id, dto);
        return result;
      } catch (e) {
        this.error = e.message;
        throw e;
      } finally {
        this.loading = false;
      }
    },

    async deleteTemplate(id) {
      this.loading = true;
      try {
        const result = await deleteTemplateService(id);
        return result;
      } catch (e) {
        this.error = e.message;
        throw e;
      } finally {
        this.loading = false;
      }
    },

    async fetchAllTemplates() {
      this.loading = true;
      try {
        this.templates = await findAllTemplatesService();
      } catch (e) {
        this.error = e.message;
      } finally {
        this.loading = false;
      }
    },

    async fetchTemplateById(id) {
      this.loading = true;
      try {
        this.currentTemplate = await findTemplateByIdService(id);
      } catch (e) {
        this.error = e.message;
      } finally {
        this.loading = false;
      }
    },

    async searchTemplateByTitle(title) {
      this.loading = true;
      try {
        this.templates = await searchTemplateByTitleService(title);
      } catch (e) {
        this.error = e.message;
      } finally {
        this.loading = false;
      }
    },
  },
});
