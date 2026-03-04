package com.automation.steps;

import com.automation.pages.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.*;
import org.junit.jupiter.api.Assertions;

/**
 * Step definitions para o feature de Checkout (Tarefa 2 — preenchimento de formulário).
 *
 * Java 17: uso de {@code CheckoutPage.FormData} record para encapsular dados do formulário.
 */
public class CheckoutSteps {

    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private ProductsPage productsPage;
    private ProductDetailPage productDetailPage;

    @Dado("o usuário adicionou um produto ao carrinho")
    public void adicionarProdutoAoCarrinho() {
        productsPage = new ProductsPage();
        productDetailPage = productsPage.tapFirstProduct();
        productDetailPage.tapAddToCart();
    }

    @Dado("o usuário está na tela do carrinho")
    public void usuarioNaTelaDoCarrinho() {
        cartPage = productDetailPage.tapBackToProducts().tapCartIcon();
        Assertions.assertTrue(cartPage.isOnCartPage(), "Tela do carrinho não está visível");
    }

    @Quando("o usuário toca no botão de checkout")
    public void tocarBotaoCheckout() {
        checkoutPage = cartPage.tapCheckout();
        Assertions.assertTrue(checkoutPage.isOnStepOne(), "Step 1 do checkout não foi exibido");
    }

    @Quando("o usuário preenche o formulário com os dados:")
    public void preencherFormulario(DataTable dataTable) {
        var row = dataTable.asMaps().get(0);
        var formData = new CheckoutPage.FormData(
                row.get("firstName"),
                row.get("lastName"),
                row.get("postalCode")
        );
        checkoutPage.fillForm(formData);
    }

    @Quando("o usuário insere o primeiro nome {string}")
    public void inserirPrimeiroNome(String firstName) {
        checkoutPage.enterFirstName(firstName);
    }

    @Quando("o usuário insere o sobrenome {string}")
    public void inserirSobrenome(String lastName) {
        checkoutPage.enterLastName(lastName);
    }

    @Quando("o usuário insere o código postal {string}")
    public void inserirCodigoPostal(String postalCode) {
        checkoutPage.enterPostalCode(postalCode);
    }

    @Quando("o usuário toca em continuar")
    public void tocarEmContinuar() {
        checkoutPage.tapContinue();
    }

    @Entao("o usuário deve ver o resumo do pedido")
    public void validarResumoDoPedido() {
        Assertions.assertTrue(checkoutPage.isOnOverview(), "Tela de resumo do pedido não foi exibida");
    }

    @Quando("o usuário confirma o pedido")
    public void confirmarPedido() {
        checkoutPage.tapFinish();
    }

    @Entao("o pedido deve ser confirmado com sucesso")
    public void validarConfirmacao() {
        Assertions.assertTrue(checkoutPage.isOrderComplete(), "Tela de confirmação do pedido não foi exibida");
    }

    @Entao("a mensagem de confirmação deve ser exibida")
    public void validarMensagemConfirmacao() {
        Assertions.assertTrue(checkoutPage.isThankYouVisible(), "Mensagem THANK YOU não foi exibida");
    }

    @Entao("uma mensagem de erro de formulário deve ser exibida")
    public void validarErroFormulario() {
        Assertions.assertTrue(checkoutPage.isErrorVisible(), "Mensagem de erro no formulário não está visível");
    }

    @Entao("a mensagem deve conter {string}")
    public void validarConteudoMensagem(String expected) {
        var actual = checkoutPage.getErrorMessage();
        Assertions.assertTrue(actual.contains(expected),
                "Mensagem esperada conter: '%s' | Atual: '%s'".formatted(expected, actual));
    }
}
