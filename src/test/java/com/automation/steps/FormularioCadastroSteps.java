package com.automation.steps;

import com.automation.pages.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.*;
import org.junit.jupiter.api.Assertions;

/**
 * Step definitions para o feature de Formulário de Cadastro (Tarefa 2).
 * Fluxo: Login → Produtos → Produto → Carrinho → Formulário de dados pessoais →
 * Confirmação
 */
public class FormularioCadastroSteps {

    private ProductsPage productsPage;
    private ProductDetailPage productDetailPage;
    private FormularioCadastroPage formularioPage;

    // ── Setup do contexto ─────────────────────────────────────────

    @Dado("o usuário autenticado navega até o formulário de cadastro")
    public void navegarAteFormulario() {
        log.info("Navegando até formulário via fluxo: Produtos → Detalhe → Carrinho → Checkout");
        productsPage = new ProductsPage();

        productDetailPage = productsPage.tapFirstProduct();
        productDetailPage.tapAddToCart();

        productsPage = productDetailPage.tapBackToProducts();
        var cartPage = productsPage.tapCartIcon();

        formularioPage = cartPage.tapCheckout();
        Assertions.assertTrue(formularioPage.formularioVisivel(),
                "Formulário de cadastro não está visível");
    }

    // ── Preenchimento via DataTable ───────────────────────────────

    @Quando("o usuário preenche o formulário de cadastro com:")
    public void preencherFormularioComDados(DataTable dataTable) {
        var row = dataTable.asMaps().get(0);
        var dados = new FormularioCadastroPage.DadosCadastro(
                row.get("nome"),
                row.get("sobrenome"),
                row.get("cep"));
        formularioPage.preencherFormulario(dados);
    }

    // ── Preenchimento individual (Scenario Outline) ───────────────

    @Quando("o usuário preenche o nome {string}")
    public void preencherNome(String nome) {
        formularioPage.preencherNome(nome);
    }

    @Quando("o usuário preenche o sobrenome {string}")
    public void preencherSobrenome(String sobrenome) {
        formularioPage.preencherSobrenome(sobrenome);
    }

    @Quando("o usuário preenche o CEP {string}")
    public void preencherCep(String cep) {
        formularioPage.preencherCep(cep);
    }

    // ── Ações ─────────────────────────────────────────────────────

    @Quando("o usuário confirma o formulário")
    public void confirmarFormulario() {
        formularioPage.tocarContinuar();
    }

    @Quando("o usuário finaliza o pedido")
    public void finalizarPedido() {
        formularioPage.tocarFinalizar();
    }

    // ── Validações ────────────────────────────────────────────────

    @Entao("o resumo do pedido deve ser exibido")
    public void validarResumoPedido() {
        Assertions.assertTrue(formularioPage.resumoVisivel(),
                "Resumo do pedido não está visível");
    }

    @Entao("o pedido deve ser concluído com sucesso")
    public void validarConclusaoPedido() {
        Assertions.assertTrue(formularioPage.pedidoConfirmado(),
                "Confirmação do pedido não foi exibida");
    }

    @Entao("a mensagem de agradecimento deve ser exibida")
    public void validarMensagemAgradecimento() {
        Assertions.assertTrue(formularioPage.mensagemObrigadoVisivel(),
                "Mensagem de agradecimento não foi exibida");
    }

    @Entao("um erro de validação deve ser exibido")
    public void validarErroFormulario() {
        Assertions.assertTrue(formularioPage.erroVisivel(),
                "Mensagem de erro do formulário não está visível");
    }

    @Entao("o erro deve informar {string}")
    public void validarTextoErro(String mensagemEsperada) {
        var actual = formularioPage.getMensagemErro();
        Assertions.assertTrue(actual.contains(mensagemEsperada),
                "Erro esperado conter: '%s' | Atual: '%s'".formatted(mensagemEsperada, actual));
    }

    // ── Logger ────────────────────────────────────────────────────

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FormularioCadastroSteps.class);
}
