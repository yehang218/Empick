import js from '@eslint/js'
import vue from 'eslint-plugin-vue'
import vueParser from 'vue-eslint-parser'
import babelParser from '@babel/eslint-parser'

export default [
    js.configs.recommended,

    // 1. 전역 기본 설정
    {
        languageOptions: {
            ecmaVersion: 'latest',
            sourceType: 'module',
            globals: {
                console: 'readonly',
                localStorage: 'readonly',
                FileReader: 'readonly',
                URL: 'readonly',
                Blob: 'readonly',
                FormData: 'readonly',
                process: 'readonly',
                alert: 'readonly',
                confirm: 'readonly',
                File: 'readonly'
            }
        }
    },

    // 2. apiClient.js는 모든 제한 해제
    {
        files: ['**/apiClient.js', '**/apis/apiClient.js'],
        rules: {
            'no-restricted-imports': 'off',
            'no-restricted-syntax': 'off'
        }
    },

    // 3. test 디렉토리는 모든 제한 해제
    {
        files: ['**/test/**/*', '**/tests/**/*', '**/*.test.js', '**/*.spec.js'],
        rules: {
            'no-restricted-imports': 'off',
            'no-restricted-syntax': 'off'
        }
    },

    // 4. Vue 파일 설정
    {
        files: ['**/*.vue'],
        languageOptions: {
            parser: vueParser,
            parserOptions: {
                parser: babelParser,
                requireConfigFile: false,
                ecmaVersion: 'latest',
                sourceType: 'module'
            }
        },
        plugins: {
            vue
        },
        rules: {
            'no-restricted-imports': [
                'error',
                {
                    paths: [
                        { name: 'axios', message: 'Vue 파일에서 axios 직접 import 금지. apiClient 또는 Store 레이어를 사용하세요.' }
                    ],
                    patterns: [
                        { group: ['@/apis/**', '@/api/**', '**/apis/**', '**/api/**', '*api.js', '*Api.js'], message: 'Vue 파일에서 API 모듈 직접 import 금지. Store 레이어를 사용하세요.' },
                        { group: ['**/services/**', '@/services/**', '*Service.js'], message: 'Vue 컴포넌트에서는 Service 파일을 직접 import하지 마세요. 반드시 Store 레이어를 통해 접근하세요.' }
                    ]
                }
            ]
        }
    },

    // 5. Store 파일 설정
    {
        files: ['**/stores/**/*.js', '**/*Store.js', '**/*store.js'],
        rules: {
            'no-restricted-imports': [
                'error',
                {
                    paths: [
                        { name: 'axios', message: 'Store 파일에서 axios 직접 사용 금지. Service 레이어를 사용하세요.' }
                    ],
                    patterns: [
                        { group: ['@/apis/**', '@/api/**', '**/apis/**', '**/api/**', '*api.js', '*Api.js'], message: 'Store 파일에서 API 모듈 직접 import 금지. Service 레이어를 사용하세요.' },
                        // Service는 store에서 허용
                    ]
                }
            ]
        }
    },

    // 6. Service 파일 설정 (api만 허용)
    {
        files: ['**/services/**/*.js', '**/services/**/*.ts'],
        rules: {
            'no-restricted-imports': [
                'error',
                {
                    paths: [
                        { name: 'axios', message: 'Service 파일에서 axios 직접 사용 금지. 반드시 apiClient를 import 하세요.' }
                    ],
                    patterns: [
                        { group: ['**/apis/routes/**', '**/api/routes/**', '@/apis/routes/**', '@/api/routes/**'], message: 'Service 파일에서 API 모듈 import는 서비스용 apiClient만 허용됩니다.' }
                    ]
                }
            ]
        }
    },

    // 7. 일반 JS 파일 설정 (axios 제한)
    {
        files: ['**/*.js'],
        ignores: ['**/apiClient.js', '**/apis/apiClient.js', '**/test/**/*', '**/tests/**/*', '**/*.test.js', '**/*.spec.js'],
        rules: {
            'no-restricted-imports': [
                'error',
                {
                    paths: [
                        { name: 'axios', message: 'axios는 반드시 apiClient.js를 통해 사용하세요.' }
                    ],
                    patterns: [
                        { group: ['**/apis/**', '**/api/**', '*api.js', '*Api.js', '@/apis/**', '@/api/**'], message: 'API 모듈 직접 import 금지. Service 레이어를 사용하세요.' }
                    ]
                }
            ]
        }
    }
]
