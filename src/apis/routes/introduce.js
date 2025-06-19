// 자기소개서 introduce 관련 API 라우트

export const IntroduceAPI = {
    // IntroduceCommandController
    CREATE_INTRODUCE: '/api/v1/employment/introduce',

    // IntroduceRatingResultCommandController
    CREATE_RATING_RESULT: '/api/v1/employment/introduce-rating-result',
    UPDATE_RATING_RESULT: (id) => `/api/v1/employment/introduce-rating-result/${id}`,
    DELETE_RATING_RESULT: (id) => `/api/v1/employment/introduce-rating-result/${id}`,

    // IntroduceStandardCommandController
    CREATE_STANDARD: '/api/v1/employment/introduce-standard',
    DELETE_STANDARD: (id) => `/api/v1/employment/introduce-standard/${id}`,

    // IntroduceStandardItemCommandController
    CREATE_STANDARD_ITEM: '/api/v1/employment/introduce-standard-item',
    DELETE_STANDARD_ITEM: (id) => `/api/v1/employment/introduce-standard-item/${id}`,

    // IntroduceTemplateCommandController
    CREATE_TEMPLATE: '/api/v1/employment/introduce-template',
    DELETE_TEMPLATE: (id) => `/api/v1/employment/introduce-template/${id}`,

    // IntroduceTemplateItemCommandController
    CREATE_TEMPLATE_ITEM: '/api/v1/employment/introduce-template-item',
    DELETE_TEMPLATE_ITEM: (id) => `/api/v1/employment/introduce-template-item/${id}`,

    // IntroduceTemplateItemResponseCommandController (추가 필요시 여기에)
    CREATE_TEMPLATE_ITEM_RESPONSE: '/api/v1/employment/introduce-template-item-response',

    // ===================== 조회(쿼리) API =====================

    // IntroduceQueryController
    GET_ALL_INTRODUCE: '/api/v1/employment/introduce',

    // IntroduceResultQueryController
    GET_ALL_INTRODUCE_RESULTS: '/api/v1/employment/introduce-result',

    // IntroduceStandardQueryController
    GET_ALL_STANDARDS: '/api/v1/employment/introduce-standard',
    GET_ALL_STANDARD_ITEMS: '/api/v1/employment/introduce-standard/item',

    // IntroduceTemplateQueryController
    GET_ALL_TEMPLATES: '/api/v1/employment/introduce-template',
    GET_ALL_TEMPLATE_ITEMS: '/api/v1/employment/introduce-template/item',
    GET_TEMPLATE_BY_ID: (id) => `/api/v1/employment/introduce-template/${id}`,

    // IntroduceTemplateItemResponseQueryController
    GET_ALL_TEMPLATE_ITEM_RESPONSES: '/api/v1/employment/introduce-template-item-response',
}; 