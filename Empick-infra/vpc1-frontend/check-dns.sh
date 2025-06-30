#!/bin/bash

echo "🌐 Empick.shop DNS 전파 모니터링"
echo "=================================="
echo "가비아에서 네임서버를 다음으로 변경했는지 확인하세요:"
echo "- ns-1008.awsdns-62.net"
echo "- ns-1294.awsdns-33.org"
echo "- ns-1807.awsdns-33.co.uk" 
echo "- ns-58.awsdns-07.com"
echo ""

while true; do
    clear
    echo "🌐 Empick.shop DNS 전파 상태 체크"
    echo "=================================="
    echo "⏰ $(date '+%Y-%m-%d %H:%M:%S')"
    echo ""
    
    echo "🔍 1. 네임서버 확인:"
    NS_RESULT=$(dig empick.shop NS +short 2>/dev/null)
    if [[ -n "$NS_RESULT" ]]; then
        echo "✅ 네임서버 발견:"
        echo "$NS_RESULT" | sed 's/^/   /'
        NS_OK=true
    else
        echo "❌ 네임서버 아직 전파 안됨"
        NS_OK=false
    fi
    
    echo ""
    echo "🔍 2. A 레코드 확인 (empick.shop):"
    A_RESULT=$(dig empick.shop A +short 2>/dev/null)
    if [[ -n "$A_RESULT" ]]; then
        echo "✅ A 레코드 발견:"
        echo "$A_RESULT" | sed 's/^/   /'
        A_OK=true
    else
        echo "❌ A 레코드 아직 전파 안됨"
        A_OK=false
    fi
    
    echo ""
    echo "🔍 3. WWW 서브도메인 확인:"
    WWW_RESULT=$(dig www.empick.shop A +short 2>/dev/null)
    if [[ -n "$WWW_RESULT" ]]; then
        echo "✅ WWW 레코드 발견:"
        echo "$WWW_RESULT" | sed 's/^/   /'
        WWW_OK=true
    else
        echo "❌ WWW 레코드 아직 전파 안됨"
        WWW_OK=false
    fi
    
    echo ""
    echo "📊 전체 상태:"
    if [[ "$NS_OK" == true && "$A_OK" == true && "$WWW_OK" == true ]]; then
        echo "🎉 DNS 전파 완료! SSL 인증서 검증을 진행할 수 있습니다."
        echo ""
        echo "다음 명령어로 SSL 재시도:"
        echo "terraform apply -var-file=\"terraform.tfvars\" -auto-approve"
        break
    else
        echo "⏳ DNS 전파 진행 중... (10초 후 재확인)"
        echo ""
        echo "💡 팁: 가비아에서 네임서버 변경 후 보통 10-30분 소요"
        echo "❌ 문제 지속 시: 가비아 네임서버 설정을 다시 확인하세요"
    fi
    
    echo ""
    echo "🛑 중단하려면 Ctrl+C"
    sleep 10
done
