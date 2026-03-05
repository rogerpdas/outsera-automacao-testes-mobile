#!/usr/bin/env bash
set -euo pipefail

CUCUMBER_TAGS="${1:-@smoke}"

echo "⏳ Aguardando emulador ficar pronto..."
adb wait-for-device
adb shell 'while [[ -z $(getprop sys.boot_completed) ]]; do sleep 2; done'
echo "✅ Emulador pronto: $(adb devices)"

echo "🚀 Iniciando Appium em background..."
appium --address 127.0.0.1 --port 4723 --log reports/appium.log --log-timestamp --log-no-colors &
APPIUM_PID=$!
echo "Appium PID: $APPIUM_PID"

echo "⏳ Aguardando Appium inicializar..."
for i in $(seq 1 15); do
  if curl -sf http://127.0.0.1:4723/status > /dev/null 2>&1; then
    echo "✅ Appium respondendo após ${i}s"
    break
  fi
  echo "  tentativa $i/15..."
  sleep 1
done

curl -s http://127.0.0.1:4723/status | python3 -m json.tool

echo "🧪 Executando testes Maven... (tags: $CUCUMBER_TAGS)"
mvn test -Dplatform=android -DappiumUrl=http://127.0.0.1:4723 -DdeviceName="emulator-5554" -DplatformVersion="12.0" "-Dcucumber.filter.tags=${CUCUMBER_TAGS}" --no-transfer-progress
MAVEN_EXIT=$?

echo "🛑 Encerrando Appium (PID $APPIUM_PID)..."
kill $APPIUM_PID 2>/dev/null || true
wait $APPIUM_PID 2>/dev/null || true
echo "✅ Appium encerrado."

echo "🛑 Desconectando ADB..."
adb -s emulator-5554 emu kill 2>/dev/null || true
sleep 2
adb kill-server 2>/dev/null || true
echo "✅ ADB desconectado."

echo "🏁 Script finalizado com código $MAVEN_EXIT"
exit $MAVEN_EXIT
