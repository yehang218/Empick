export const testRoutes = [
    {
        path: '/test/list-view-test-page',
        name: 'ListViewTestPage',
        component: () => import('@/views/test/ListViewTestPage.vue')
    },
    {
        path: '/test/mj',
        name: 'MJTestMainPage',
        component: () => import('@/views/test/MJTestMainPage.vue'),
        props: true
    },
    {
        path: '/test/mj/calendar',
        name: 'MJCalendarTestPage',
        component: () => import('@/views/test/MJCalendarTestPage.vue'),
        props: true
    },
    {
        path: '/test/mj/evaluationCriteriaList',
        name: 'MJEvaluationCriteriaListTestPage',
        component: () => import('@/views/test/MJEvaluationCriteriaListTestPage.vue'),
        props: true
    },
    {
        path: '/test/mj/evaluationScore',
        name: 'MJEvaluationScoreTestPage',
        component: () => import('@/views/test/MJEvaluationScoreTestPage.vue')
    },
    {
        path: '/test/list',
        name: 'ListView',
        component: () => import('@/components/common/ListView.vue')
    },
    {
        path: '/test/mj/mailReceiverList',
        name: 'MJMailReceiverListTestPage',
        component: () => import('@/views/test/MJMailReceiverListTestPage.vue')
    },
    {
        path: '/test/mj/messageInputBox',
        name: 'MJMessageInputBoxTestPage',
        component: () => import('@/views/test/MJMessageInputBoxTestPage.vue')
    },
    {
        path: '/test/mj/oneColumnList',
        name: 'MJOneColumnListTestPage',
        component: () => import('@/views/test/MJOneColumnListTestPage.vue')
    },
    {
        path: '/WSPage',
        name: 'WSPage',
        component: () => import('@/views/test/WSPage.vue')
    }
]; 