export const MailAPI = {
    BASE: '/api/v1/employment/mail',
    CREATE: '/api/v1/employment/mail/create',
    SEND: '/api/v1/employment/mail/send',
    SEND_JOBTEST: (id, senderId) => `/api/v1/employment/mail/send/jobtest/${id}?senderId=${senderId}`,
    SEND_INTERVIEW: (id, senderId) => `/api/v1/employment/mail/send/interview/${id}?senderId=${senderId}`,
    GET_ALL: '/api/v1/employment/mail',
    GET_BY_ID: (id) => `/api/v1/employment/mail/${id}`,
    GET_BY_EMAIL: (email) => `/api/v1/employment/mail/email?email=${email}`,
};

export const MailTemplateAPI = {
    BASE: '/api/v1/employment/mailTemplate',
    CREATE: '/api/v1/employment/mailTemplate',
    UPDATE: (id) => `/api/v1/employment/mailTemplate/${id}`,
    DELETE: (id) => `/api/v1/employment/mailTemplate/${id}`,
    GET_ALL: '/api/v1/employment/mailTemplate',
    GET_BY_ID: (id) => `/api/v1/employment/mailTemplate/${id}`,
    SEARCH_BY_TITLE: (title) => `/api/v1/employment/mailTemplate/search?title=${title}`,
};
