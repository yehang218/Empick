// src/router/career.routes.js
export const careerRoutes = [
  {
    path: '/career/recruitments',
    name: 'CareerRecruitmentList',
    component: () => import('@/views/career/CareerRecruitmentList.vue'),
    meta: {
      requiresAuth: false,
      hideHeader: true,
      hideSidebar: true
    }
  },

   {
    path: '/career/welfare',
    name: 'CareerWelfarePage',
    component: () => import('@/views/career/CareerWelfarePage.vue'),
     meta: {
      requiresAuth: false,
      hideHeader: true,
      hideSidebar: true
     }
  },
];
