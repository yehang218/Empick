<!-- src/components/DynamicTable.vue -->
<template>
    <v-container>
        <v-table class="mt-5">
            <thead>
                <tr>
                    <th v-if="showCheckbox" style="width: 80px;">선택</th>
                    <th v-for="header in headers" :key="header.key">
                        {{ header.label }}
                    </th>
                    <th></th> <!-- optional: actions column -->
                </tr>
            </thead>
            <tbody>
                <tr v-for="(item, index) in data" :key="index" @click="$emit('item-click', item)"
                    style="cursor: pointer;">
                    <td v-if="showCheckbox" @click.stop>
                        <v-btn size="small" icon :color="item.selected ? 'primary' : 'grey-lighten-1'" variant="tonal"
                            @click.stop="$emit('toggle-select', item.id)">
                            <v-icon>
                                {{ item.selected ? 'mdi-checkbox-marked' : 'mdi-checkbox-blank-outline' }}
                            </v-icon>
                        </v-btn>
                    </td>
                    <td v-for="header in headers" :key="header.key">
                        <template v-if="header.key === 'avatarName' && item.avatar && item.name">
                            <div class="d-flex align-center">
                                <v-avatar class="me-2" size="32">
                                    <v-img :src="item.avatar" />
                                </v-avatar>
                                <div>
                                    <div>{{ item.name }}</div>
                                    <div class="text-caption grey--text">{{ item.position }}</div>
                                </div>
                            </div>
                        </template>
                        <template v-else-if="header.key === 'department' && item.department">
                            <div>{{ item.department }}</div>
                            <div class="text-caption grey--text">{{ item.departmentPosition }}</div>
                        </template>
                        <template v-else-if="header.key === 'status' && item.status">
                            <v-chip :color="item.status === '처리완료' ? 'green lighten-4' : 'red lighten-4'"
                                :text-color="item.status === '처리완료' ? 'green darken-2' : 'red darken-2'" small>
                                {{ item.status }}
                            </v-chip>
                        </template>
                        <template v-else-if="header.key === 'isMyTurn'">
                            <v-chip v-if="item.canApproveChip" color="success" text-color="white" small>결재 가능</v-chip>
                            <span v-else>{{ item.isMyTurn }}</span>
                        </template>
                        <template v-else>
                            {{ item[header.key] }}
                        </template>
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
    name: 'DynamicTable',
    props: {
        headers: {
            type: Array,
            required: true,
            default: () => []
        },
        data: {
            type: Array,
            required: true,
            default: () => []
        },
        showCheckbox: {
            type: Boolean,
            default: false
        }
    }
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

.text-caption {
    font-size: 12px;
    color: #9A9A9A;
    margin-top: 4px;
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
