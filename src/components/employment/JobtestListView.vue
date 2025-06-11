<template>
    <v-table>
        <thead>
            <tr>
                <th v-if="showCheckbox" style="width: 48px;"></th>
                <th v-for="header in headers" :key="header.key">{{ header.label }}</th>
                <th v-if="showArrow"></th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(item, idx) in items" :key="idx">
                <td v-if="showCheckbox">
                    <v-checkbox v-model="item.selected" :ripple="false" hide-details density="compact" />
                </td>
                <td v-for="header in headers" :key="header.key">
                    <slot :name="`cell-${header.key}`" :item="item" :value="item[header.key]" :header="header">
                        <!-- 기본 분기 렌더링 -->
                        <template v-if="header.type === 'avatar' && item[header.key]">
                            <div class="d-flex align-center">
                                <v-avatar class="me-2" size="28">
                                    <v-img :src="item[header.key].avatar" />
                                </v-avatar>
                                <span>{{ item[header.key].name }}</span>
                            </div>
                        </template>
                        <template v-else-if="header.type === 'chip'">
                            <v-chip :color="chipColor(item[header.key])" size="small">{{ item[header.key] }}</v-chip>
                        </template>
                        <template v-else>
                            {{ item[header.key] }}
                        </template>
                    </slot>
                </td>
                <td v-if="showArrow">
                    <v-icon>mdi-arrow-right</v-icon>
                </td>
            </tr>
        </tbody>
    </v-table>
</template>

<script setup>
const props = defineProps({
    headers: { type: Array, required: true },
    items: { type: Array, required: true },
    showCheckbox: { type: Boolean, default: false },
    showArrow: { type: Boolean, default: true }
})

function chipColor(val) {
    if (val === '쉬움' || val === '대기') return 'green lighten-4'
    if (val === '보통' || val === '처리중') return 'blue lighten-4'
    if (val === '어려움' || val === '완료') return 'red lighten-4'
    return 'grey lighten-4'
}
</script>

<style scoped>
th {
    font-weight: bold;
    color: #9A9A9A;
    text-align: left;
    padding: 12px 16px;
    font-size: 14px;
}

td {
    vertical-align: middle;
    padding: 16px;
    font-size: 14px;
    color: #333333;
    border-bottom: 1px solid #EEEEEE;
}

.v-table {
    width: 100%;
    border-collapse: collapse;
}

tr:hover {
    background-color: #F9F9F9;
}

.v-avatar {
    margin-right: 12px;
}

.v-chip {
    font-size: 12px;
    font-weight: 500;
}

thead th {
    background-color: #F8F8F8;
    position: sticky;
    top: 0;
    z-index: 1;
}

.v-icon {
    cursor: pointer;
    color: #9A9A9A;
}

tbody tr td:last-child {
    text-align: right;
    padding-right: 24px;
}

tbody tr td:first-child {
    display: flex;
    align-items: center;
}

tbody tr td {
    height: 56px;
}
</style>
