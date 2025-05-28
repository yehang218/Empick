<template>
    <v-container>
        <v-table class="mt-5">
            <thead>
                <tr>
                    <th>이름</th>
                    <th>사번</th>
                    <th>이메일</th>
                    <th>연락처</th>
                    <th>부서</th>
                    <th>처리상태</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(user, index) in users" :key="index">
                    <td class="d-flex align-center">
                        <v-avatar class="me-2" size="32">
                            <v-img :src="user.avatar" />
                        </v-avatar>
                        <div>
                            <div>{{ user.name }}</div>
                            <div class="text-caption grey--text">{{ user.position }}</div>
                        </div>
                    </td>
                    <td>{{ user.id }}</td>
                    <td>{{ user.email }}</td>
                    <td>{{ user.phone }}</td>
                    <td>
                        <div>{{ user.department }}</div>
                        <div class="text-caption grey--text">{{ user.departmentPosition }}</div>
                    </td>
                    <td>
                        <v-chip :color="user.status === '처리완료' ? 'green lighten-4' : 'red lighten-4'"
                            :text-color="user.status === '처리완료' ? 'green darken-2' : 'red darken-2'" small>
                            {{ user.status }}
                        </v-chip>
                    </td>
                    <td>
                        <v-icon>mdi-arrow-right</v-icon>
                    </td>
                </tr>
            </tbody>
        </v-table>
    </v-container>
</template>

<script>
export default {
    data() {
        return {
            users: [
                {
                    name: 'Brooklyn Simmons',
                    position: '대리',
                    id: '87364523',
                    email: 'brooklyns@mail.com',
                    phone: '(603) 555-0123',
                    department: '인사',
                    departmentPosition: '팀장',
                    status: '처리완료',
                    avatar: 'https://cdn.vuetifyjs.com/images/john.jpg'
                },
                {
                    name: 'Kristin Watson',
                    position: '사원',
                    id: '93874563',
                    email: 'kristinw@mail.com',
                    phone: '(219) 555-0114',
                    department: '백엔드/개발',
                    departmentPosition: 'PM',
                    status: '미처리',
                    avatar: 'https://cdn.vuetifyjs.com/images/john.jpg'
                },
                {
                    name: 'Jacob Jones',
                    position: '사원',
                    id: '23847569',
                    email: 'jacobj@mail.com',
                    phone: '(319) 555-0115',
                    department: '회계',
                    departmentPosition: '미지정',
                    status: '처리완료',
                    avatar: 'https://cdn.vuetifyjs.com/images/john.jpg'
                },
                {
                    name: 'Cody Fisher',
                    position: '주임',
                    id: '39485632',
                    email: 'codyf@mail.com',
                    phone: '(229) 555-0109',
                    department: '인사',
                    departmentPosition: '미지정',
                    status: '처리완료',
                    avatar: 'https://cdn.vuetifyjs.com/images/john.jpg'
                },
                // 다른 항목들도 동일하게 추가 가능
            ]
        }
    }
}
</script>

<style scoped>
th {
    font-weight: bold;
}

td {
    vertical-align: middle;
}
</style>
