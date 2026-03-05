# Mobile Automation — Desafio Outsera

> Automação de testes mobile com **Java 17 + Appium 2 + Cucumber 7 + ExtentReports**, com pipeline CI/CD no **GitHub Actions**.

---

## Descrição

Projeto de automação mobile para o desafio da Outsera cobrindo dois cenários principais:

- **Tarefa 1 — Login e Navegação:** Testa autenticação, navega entre telas e valida elementos nativos (botões, títulos, mensagens de erro).
- **Tarefa 2 — Preenchimento de Formulário:** Automatiza o formulário de checkout com validação de campos obrigatórios e confirmação de pedido.

**App alvo:** [Sauce Labs Sample App (Swag Labs)](https://github.com/saucelabs/sample-app-mobile) — APK com login, listagem de produtos e checkout.

---

## Recursos Java 17 utilizados

| Feature              | Onde é usado                              |
|----------------------|-------------------------------------------|
| **Records**          | `AppConfig`, `CheckoutPage.FormData`, `Platform.Android/Ios` |
| **Sealed Classes**   | `Platform` (garante exhaustiveness no switch) |
| **Switch Expression**| `DriverManager`, `NavigationSteps`         |
| **Text Blocks**      | Hooks (banners de log), `ConfigLoader`     |
| **`var`**            | Em todos os métodos para inferência local  |
| **`String.formatted()`** | Mensagens de erro e log descritivos   |
| **`Path` + `Files`** | `ScreenshotUtils` (NIO moderno)           |
| **`List.getFirst()`**| `ProductsPage`, `CheckoutSteps`            |

---

## Estrutura de Pastas

```
mobile-automation/
├── .github/
│   └── workflows/
│       └── mobile-ci.yml               # Pipeline CI/CD
├── apps/
│   └── sauce-labs-demo.apk             # APK (baixar antes de executar)
├── reports/
│   └── screenshots/                    # Evidências geradas automaticamente
├── src/
│   └── test/
│       ├── java/com/automation/
│       │   ├── config/
│       │   │   ├── AppConfig.java       # Record imutável de configuração (Java 17)
│       │   │   ├── ConfigLoader.java    # Singleton com text blocks e Optional
│       │   │   └── Platform.java       # Sealed interface + records Android/Ios
│       │   ├── drivers/
│       │   │   └── DriverManager.java  # ThreadLocal + switch expression
│       │   ├── hooks/
│       │   │   └── Hooks.java          # Before/After com text blocks
│       │   ├── pages/
│       │   │   ├── BasePage.java       # Métodos base com var e NIO
│       │   │   ├── LoginPage.java
│       │   │   ├── ProductsPage.java
│       │   │   ├── ProductDetailPage.java
│       │   │   ├── CartPage.java
│       │   │   └── CheckoutPage.java   # Record interno FormData
│       │   ├── runners/
│       │   │   ├── TestRunner.java     # Suite completa
│       │   │   └── SmokeTestRunner.java
│       │   ├── steps/
│       │   │   ├── LoginSteps.java
│       │   │   ├── NavigationSteps.java # Switch expression nos botões
│       │   │   └── CheckoutSteps.java   # Uso do record FormData
│       │   └── utils/
│       │       └── ScreenshotUtils.java # NIO (Path + Files)
│       └── resources/
│           ├── features/
│           │   ├── login.feature
│           │   ├── navigation.feature
│           │   └── checkout.feature
│           ├── config.yaml
│           ├── extent.properties
│           ├── extent-config.xml
│           └── logback-test.xml
├── pom.xml
├── run-tests.sh                        # Script de execução no CI
└── README.md
```

---

## Versões Utilizadas

| Tecnologia              | Versão     |
|-------------------------|------------|
| **Java**                | **17 LTS** |
| Maven                   | 3.8+       |
| Appium Java Client      | 8.6.0      |
| Appium Server           | 2.x        |
| UiAutomator2 Driver     | Latest     |
| Cucumber                | 7.14.0     |
| JUnit Platform          | 5.10.0     |
| ExtentReports           | 5.1.1      |
| Selenium                | 4.15.0     |
| Sauce Labs Demo App     | 1.3.0      |
| Android API Level       | 31 (12.0)  |
| Node.js                 | 18+        |

---

## Dependências — Como Instalar

### Pré-requisitos

1. **Java 17 LTS**
   ```bash
   java -version   # deve exibir 17.x
   # Download: https://adoptium.net/temurin/releases/?version=17
   ```

2. **Maven 3.8+**
   ```bash
   mvn -version
   # Download: https://maven.apache.org/download.cgi
   ```

3. **Node.js 18+ e Appium 2**
   ```bash
   node -v
   npm install -g appium@latest
   appium driver install uiautomator2    # Android
   appium driver install xcuitest        # iOS (apenas macOS)
   ```

4. **Android Studio + Emulador (Android)**
   - Instalar [Android Studio](https://developer.android.com/studio)
   - Criar AVD com API Level 31 (Android 12, Pixel 4)
   - Ou conectar dispositivo físico com depuração USB ativa

5. **APK do app de testes**
   ```bash
   mkdir -p apps
   curl -fsSL \
     "https://github.com/saucelabs/sample-app-mobile/releases/download/2.7.1/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk" \
     -o apps/swag-labs.apk
   ```

6. **Instalar dependências Maven**
   ```bash
   mvn clean install -DskipTests
   ```

---

## Como Executar os Testes

### 1. Iniciar Appium Server
```bash
appium --address 127.0.0.1 --port 4723
```

### 2. Iniciar Emulador Android
```bash
emulator -list-avds                # listar emuladores
emulator -avd <nome_do_avd> &      # iniciar
adb devices                        # confirmar conexão
```

### 3. Executar

| Comando | O que faz |
|---------|-----------|
| `mvn test` | Suite completa (regressão) |
| `mvn test -Dcucumber.filter.tags="@smoke"` | Smoke tests |
| `mvn test -Dcucumber.filter.tags="@login"` | Só login |
| `mvn test -Dcucumber.filter.tags="@checkout"` | Só formulário |
| `mvn test -Dcucumber.filter.tags="@navigation"` | Só navegação |

**Com override de configuração (CI/CD):**
```bash
mvn test \
  -Dplatform=android \
  -DdeviceName="emulator-5554" \
  -DplatformVersion="12.0" \
  -DappiumUrl="http://127.0.0.1:4723" \
  -Dcucumber.filter.tags="@regression"
```

---

## Relatórios

Gerados automaticamente após a execução:

```bash
# ExtentReports (principal — dark theme)
open reports/index.html
```

> **Captura de Tela (Screenshots):** Em caso de falha no teste, o framework captura automaticamente uma imagem da tela e anexa tanto ao diretório `reports/screenshots/` quanto **embutida diretamente no relatório do ExtentReports**.

| Relatório | Caminho |
|----------------------------|-----------------------------------------------------------|
| **ExtentReports (HTML)**   | `reports/index.html`                               |
| **Evidências (Screenshots)** | `reports/screenshots/`                             |       |
| **Log completo**           | `reports/automation.log`                                  |

---

## CI/CD — GitHub Actions

Pipeline em `.github/workflows/mobile-ci.yml`:

Observação: O pipeline está configurado para rodar apenas no Windows, mas pode ser adaptado para outras plataformas.
Além disso, após a conclusão do pipeline, os relatórios são gerados automaticamente e podem ser acessados no GitHub Actions Summary, ou
através da url para o gitPage - https://rogerpdas.github.io/outsera-automacao-testes-mobile/index.html.

**Gatilhos:**
- Push para `main` ou `develop`
- Pull Request para `main`
- Execução manual (`workflow_dispatch`) com escolha de tags e plataforma

**Jobs:**
1. **Build** — compila e valida o projeto
2. **Android Tests** — sobe emulador, instala Appium, baixa APK, executa testes
3. **Sumário** — gera resumo no GitHub Actions Summary

**Artefatos salvos por 30 dias:**
- Relatórios HTML/JSON/XML
- Screenshots de falhas
- Log do Appium

---

## Tags

| Tag           | Descrição                              |
|---------------|----------------------------------------|
| `@smoke`      | Testes críticos — verificação rápida   |
| `@regression` | Suite completa de regressão            |
| `@login`      | Cenários de autenticação               |
| `@checkout`   | Cenários de formulário/checkout        |
| `@navigation` | Cenários de navegação entre telas      |

---

## Arquitetura e Boas Práticas

- **Page Object Model (POM):** cada tela tem sua classe com locators e ações encapsulados
- **Locators nativos:** `@AndroidFindBy` + `@iOSXCUITFindBy` para suporte cross-platform
- **ThreadLocal Driver:** suporte à execução paralela com isolamento de sessão
- **Execução Unificada:** O script `run-tests.sh` encapsula a inicialização do Appium e execução do Maven, resolvendo problemas de persistência no CI/CD e simplificando execuções complexas.
- **Sealed Platform:** garante que novas plataformas sejam tratadas pelo compilador
- **Imutabilidade:** `AppConfig` e `FormData` como records — thread-safe por design
- **Configuração externalizada:** `config.yaml` + override via `-D` para CI/CD
- **Evidências automáticas:** screenshots em qualquer cenário com falha
- **Relatórios Customizados:** `ExtentReportManager` gerencia métricas de execução e exporta `summary.properties` para integração rica no GitHub Actions
- **BDD em português:** cenários legíveis por toda a equipe
