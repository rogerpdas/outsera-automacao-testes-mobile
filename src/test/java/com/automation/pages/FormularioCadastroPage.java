package com.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

/**
 * Page Object da tela de Formulário de Informações Pessoais do Checkout.
 * Representa a Tarefa 2: preenchimento de formulário com dados do usuário.
 *
 * O app Swag Labs possui um formulário de dados pessoais acessível via:
 * Produtos → Carrinho → Checkout → Preencher dados
 *
 * Java 17: record interno {@code DadosCadastro} para encapsular os dados do formulário.
 */
public class FormularioCadastroPage extends BasePage {

    /**
     * Record imutável com os dados do formulário — Java 17.
     */
    public record DadosCadastro(String nome, String sobrenome, String cep) {
        public DadosCadastro {
            if (nome      == null) nome      = "";
            if (sobrenome == null) sobrenome = "";
            if (cep       == null) cep       = "";
        }
    }

    // ── Campos do formulário ──────────────────────────────────────

    @AndroidFindBy(accessibility = "test-First Name")
    @iOSXCUITFindBy(accessibility = "First Name")
    private WebElement campoNome;

    @AndroidFindBy(accessibility = "test-Last Name")
    @iOSXCUITFindBy(accessibility = "Last Name")
    private WebElement campoSobrenome;

    @AndroidFindBy(accessibility = "test-Zip/Postal Code")
    @iOSXCUITFindBy(accessibility = "Zip/Postal Code")
    private WebElement campoCep;

    // ── Botões ────────────────────────────────────────────────────

    @AndroidFindBy(accessibility = "test-CONTINUE")
    @iOSXCUITFindBy(accessibility = "CONTINUE")
    private WebElement botaoContinuar;

    @AndroidFindBy(accessibility = "test-CANCEL")
    @iOSXCUITFindBy(accessibility = "CANCEL")
    private WebElement botaoCancelar;

    // ── Erro ─────────────────────────────────────────────────────

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Error message']/android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='Error message']")
    private WebElement mensagemErro;

    // ── Resumo do pedido (step 2) ─────────────────────────────────

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='CHECKOUT: OVERVIEW']")
    @iOSXCUITFindBy(accessibility = "CHECKOUT: OVERVIEW")
    private WebElement tituloPedidoResumo;

    @AndroidFindBy(accessibility = "test-FINISH")
    @iOSXCUITFindBy(accessibility = "FINISH")
    private WebElement botaoFinalizar;

    // ── Confirmação (step 3) ──────────────────────────────────────

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='CHECKOUT: COMPLETE!']")
    @iOSXCUITFindBy(accessibility = "CHECKOUT: COMPLETE!")
    private WebElement tituloPedidoConfirmado;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'THANK YOU')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@name,'THANK YOU')]")
    private WebElement mensagemObrigado;

    @AndroidFindBy(accessibility = "test-BACK HOME")
    @iOSXCUITFindBy(accessibility = "BACK HOME")
    private WebElement botaoVoltarHome;

    // ── Ações ─────────────────────────────────────────────────────

    public FormularioCadastroPage preencherNome(String valor) {
        log.info("Preenchendo nome: {}", valor);
        fill(campoNome, valor);
        return this;
    }

    public FormularioCadastroPage preencherSobrenome(String valor) {
        log.info("Preenchendo sobrenome: {}", valor);
        fill(campoSobrenome, valor);
        return this;
    }

    public FormularioCadastroPage preencherCep(String valor) {
        log.info("Preenchendo CEP: {}", valor);
        fill(campoCep, valor);
        return this;
    }

    /** Preenche todos os campos via record DadosCadastro */
    public FormularioCadastroPage preencherFormulario(DadosCadastro dados) {
        log.info("Preenchendo formulário: {} {} — CEP {}", dados.nome(), dados.sobrenome(), dados.cep());
        preencherNome(dados.nome());
        preencherSobrenome(dados.sobrenome());
        preencherCep(dados.cep());
        return this;
    }

    public FormularioCadastroPage tocarContinuar() {
        log.info("Tocando em CONTINUE");
        scrollToAndTap(botaoContinuar);
        return this;
    }

    public FormularioCadastroPage tocarFinalizar() {
        log.info("Finalizando pedido");
        scrollToAndTap(botaoFinalizar);
        return this;
    }

    public ProductsPage tocarVoltarHome() {
        log.info("Voltando para home");
        tap(botaoVoltarHome);
        return new ProductsPage();
    }

    // ── Validações ────────────────────────────────────────────────

    public boolean formularioVisivel()       { return isVisible(campoNome); }
    public boolean resumoVisivel()           { return isVisible(tituloPedidoResumo); }
    public boolean pedidoConfirmado()        { return isVisible(tituloPedidoConfirmado); }
    public boolean mensagemObrigadoVisivel() { return isVisible(mensagemObrigado); }
    public boolean erroVisivel()             { return isVisible(mensagemErro); }
    public String  getMensagemErro()         { return readText(mensagemErro); }
    public String  getMensagemObrigado()     { return readText(mensagemObrigado); }
}
