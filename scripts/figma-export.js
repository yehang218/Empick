const fs = require('fs')
const path = require('path')

// Vue 컴포넌트에서 디자인 토큰 추출
const extractDesignTokens = () => {
  const components = {
    common: [
      'Header.vue',
      'AlertModal.vue', 
      'Search.vue',
      'PageHeader.vue',
      'Pagination.vue',
      'Modal.vue',
      'MainSidebar.vue',
      'ListView.vue',
      'CircleLoading.vue',
      'Calendar.vue'
    ],
    employment: [
      // 취업 관련 컴포넌트들
    ],
    attendance: [
      // 근태 관련 컴포넌트들  
    ]
  }

  // 피그마 디자인 토큰 구조
  const designTokens = {
    colors: {
      primary: '#1976d2',
      secondary: '#424242',
      success: '#4caf50',
      warning: '#ff9800',
      error: '#f44336',
      info: '#2196f3'
    },
    typography: {
      h1: { fontSize: '2rem', fontWeight: 500 },
      h2: { fontSize: '1.5rem', fontWeight: 500 },
      body1: { fontSize: '1rem', fontWeight: 400 },
      caption: { fontSize: '0.75rem', fontWeight: 400 }
    },
    spacing: {
      xs: '4px',
      sm: '8px', 
      md: '16px',
      lg: '24px',
      xl: '32px'
    },
    components: {}
  }

  // 각 컴포넌트별 스타일 정보 추출
  Object.keys(components).forEach(category => {
    designTokens.components[category] = {}
    
    components[category].forEach(componentFile => {
      const componentPath = path.join(__dirname, '../src/components', category, componentFile)
      
      if (fs.existsSync(componentPath)) {
        const content = fs.readFileSync(componentPath, 'utf8')
        
        // Vue 컴포넌트 분석
        const componentInfo = analyzeVueComponent(content, componentFile)
        designTokens.components[category][componentFile.replace('.vue', '')] = componentInfo
      }
    })
  })

  return designTokens
}

// Vue 컴포넌트 분석 함수
const analyzeVueComponent = (content, filename) => {
  const componentInfo = {
    name: filename.replace('.vue', ''),
    props: [],
    styles: {},
    layout: {},
    figmaSpecs: {}
  }

  // props 추출
  const propsMatch = content.match(/props:\s*{([^}]+)}/s)
  if (propsMatch) {
    // props 정보 파싱
    componentInfo.props = extractProps(propsMatch[1])
  }

  // 스타일 추출 (<style> 태그)
  const styleMatch = content.match(/<style[^>]*>(.*?)<\/style>/s)
  if (styleMatch) {
    componentInfo.styles = extractStyles(styleMatch[1])
  }

  // 템플릿 구조 분석
  const templateMatch = content.match(/<template>(.*?)<\/template>/s)
  if (templateMatch) {
    componentInfo.layout = analyzeTemplate(templateMatch[1])
  }

  // 피그마 변환을 위한 메타데이터
  componentInfo.figmaSpecs = {
    type: determineComponentType(componentInfo),
    variants: extractVariants(content),
    states: extractStates(content),
    responsive: checkResponsive(content)
  }

  return componentInfo
}

// 컴포넌트 타입 결정
const determineComponentType = (componentInfo) => {
  const name = componentInfo.name.toLowerCase()
  
  if (name.includes('modal')) return 'overlay'
  if (name.includes('button')) return 'interactive'
  if (name.includes('input') || name.includes('search')) return 'form'
  if (name.includes('header') || name.includes('sidebar')) return 'navigation'
  if (name.includes('card') || name.includes('list')) return 'content'
  if (name.includes('loading')) return 'feedback'
  
  return 'component'
}

// props 추출
const extractProps = (propsString) => {
  // 간단한 props 파싱 (실제로는 더 복잡한 파싱 필요)
  const props = []
  const lines = propsString.split('\n')
  
  lines.forEach(line => {
    const match = line.match(/(\w+):\s*{([^}]+)}/)
    if (match) {
      props.push({
        name: match[1],
        config: match[2]
      })
    }
  })
  
  return props
}

// 스타일 추출
const extractStyles = (styleString) => {
  const styles = {}
  
  // CSS 변수 추출
  const cssVars = styleString.match(/--[\w-]+:\s*[^;]+/g) || []
  styles.cssVariables = cssVars

  // 색상 추출
  const colors = styleString.match(/#[0-9a-fA-F]{3,6}/g) || []
  styles.colors = [...new Set(colors)]

  // 폰트 크기 추출
  const fontSizes = styleString.match(/font-size:\s*[\d.]+(?:px|rem|em)/g) || []
  styles.fontSizes = [...new Set(fontSizes)]

  return styles
}

// 템플릿 구조 분석
const analyzeTemplate = (templateString) => {
  const layout = {
    rootElement: '',
    children: [],
    classes: []
  }

  // 루트 엘리먼트 찾기
  const rootMatch = templateString.match(/<(\w+)[^>]*>/)
  if (rootMatch) {
    layout.rootElement = rootMatch[1]
  }

  // 클래스명 추출
  const classMatches = templateString.match(/class="([^"]+)"/g) || []
  classMatches.forEach(match => {
    const classes = match.replace('class="', '').replace('"', '').split(' ')
    layout.classes.push(...classes)
  })

  layout.classes = [...new Set(layout.classes)]

  return layout
}

// 변형(variants) 추출
const extractVariants = (content) => {
  const variants = []
  
  // v-if, v-show 등으로 조건부 렌더링되는 부분들
  const conditionals = content.match(/v-if="[^"]+"/g) || []
  variants.push(...conditionals.map(c => c.replace('v-if="', '').replace('"', '')))

  // props에 따른 클래스 변경
  const dynamicClasses = content.match(/:class="[^"]+"/g) || []
  variants.push(...dynamicClasses)

  return variants
}

// 상태(states) 추출
const extractStates = (content) => {
  const states = []
  
  // 일반적인 상태들
  if (content.includes('loading')) states.push('loading')
  if (content.includes('disabled')) states.push('disabled')
  if (content.includes('active')) states.push('active')
  if (content.includes('hover')) states.push('hover')
  if (content.includes('focus')) states.push('focus')
  if (content.includes('error')) states.push('error')

  return states
}

// 반응형 체크
const checkResponsive = (content) => {
  const responsive = {
    breakpoints: [],
    flexbox: false,
    grid: false
  }

  // CSS 미디어 쿼리
  const mediaQueries = content.match(/@media[^{]+/g) || []
  responsive.breakpoints = mediaQueries

  // Flexbox 사용
  if (content.includes('display: flex') || content.includes('d-flex')) {
    responsive.flexbox = true
  }

  // Grid 사용
  if (content.includes('display: grid') || content.includes('grid-template')) {
    responsive.grid = true
  }

  return responsive
}

// 피그마 토큰 파일 생성
const generateFigmaTokens = () => {
  const tokens = extractDesignTokens()
  
  // JSON 형태로 저장 (Figma Tokens 플러그인 호환)
  const figmaTokens = {
    global: {
      colors: tokens.colors,
      typography: tokens.typography,
      spacing: tokens.spacing
    },
    components: tokens.components
  }

  fs.writeFileSync(
    path.join(__dirname, '../figma-tokens.json'), 
    JSON.stringify(figmaTokens, null, 2)
  )

  console.log('✅ Figma 토큰 파일 생성 완료: figma-tokens.json')
  return figmaTokens
}

// 컴포넌트 명세서 생성 (Figma 가이드용)
const generateComponentSpecs = (tokens) => {
  let specs = '# 컴포넌트 피그마 변환 가이드\n\n'
  
  specs += '## 🎨 디자인 토큰\n\n'
  specs += '### 컬러 팔레트\n'
  Object.entries(tokens.global.colors).forEach(([name, value]) => {
    specs += `- **${name}**: ${value}\n`
  })

  specs += '\n### 타이포그래피\n'
  Object.entries(tokens.global.typography).forEach(([name, value]) => {
    specs += `- **${name}**: ${value.fontSize}, weight: ${value.fontWeight}\n`
  })

  specs += '\n## 📦 컴포넌트별 변환 가이드\n\n'
  
  Object.entries(tokens.components).forEach(([category, components]) => {
    specs += `### ${category.toUpperCase()}\n\n`
    
    Object.entries(components).forEach(([name, info]) => {
      specs += `#### ${name}\n`
      specs += `- **타입**: ${info.figmaSpecs.type}\n`
      specs += `- **상태**: ${info.figmaSpecs.states.join(', ')}\n`
      specs += `- **변형**: ${info.figmaSpecs.variants.length}개\n`
      specs += `- **반응형**: ${info.figmaSpecs.responsive.flexbox ? 'Flexbox' : 'Static'}\n\n`
    })
  })

  fs.writeFileSync(
    path.join(__dirname, '../FIGMA_GUIDE.md'), 
    specs
  )

  console.log('✅ 피그마 변환 가이드 생성 완료: FIGMA_GUIDE.md')
}

// 실행
if (require.main === module) {
  console.log('🎨 Vue → Figma 변환 스크립트 시작...')
  
  const tokens = generateFigmaTokens()
  generateComponentSpecs(tokens)
  
  console.log('\n🎉 변환 완료!')
  console.log('📁 생성된 파일:')
  console.log('  - figma-tokens.json (Figma Tokens 플러그인용)')
  console.log('  - FIGMA_GUIDE.md (변환 가이드)')
  console.log('\n📋 다음 단계:')
  console.log('  1. Figma에서 "Figma Tokens" 플러그인 설치')
  console.log('  2. figma-tokens.json 파일 업로드')
  console.log('  3. FIGMA_GUIDE.md 참고하여 컴포넌트 수동 생성')
}

module.exports = {
  extractDesignTokens,
  generateFigmaTokens,
  generateComponentSpecs
} 