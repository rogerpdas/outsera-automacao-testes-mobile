package com.automation.steps;

import com.automation.pages.*;
import io.cucumber.java.pt.*;
import org.junit.jupiter.api.Assertions;

/** Step definitions para o feature de Navegação (Tarefa 1). */
public class NavigationSteps {

    private ProductsPage productsPage;
    private ProductDetailPage productDetailPage;
    private CartPage cartPage;

    @Dado("o usuário está autenticado com e-mail {string} e senha {string}")
    public void autenticarUsuario(String email, String password) {
        var loginPage = new LoginPage();
        productsPage  = loginPage.doLogin(email, password);
        Assertions.assertTrue(productsPage.isOnProductsPage(),
                "Login falhou — tela de Produtos não exibida");
    }

    @Quando("o usuário está na tela de Produtos")
    public void validarTelaDeProdutos() {
        Assertions.assertTrue(productsPage.isOnProductsPage(),
                "Usuário não está na tela de Produtos");
    }

    @Quando("o usuário toca no primeiro produto disponível")
    public void tocarPrimeiroProduto() {
        productDetailPage = productsPage.tapFirstProduct();
    }

    @Entao("a tela de detalhe do produto deve ser exibida")
    public void validarTelaDetalhe() {
        Assertions.assertTrue(productDetailPage.isOnProductDetailPage(),
                "Tela de detalhe não exibida (botão BACK TO PRODUCTS ausente)");
    }

    @Entao("o botão {string} deve estar visível")
    public void validarBotaoVisivel(String buttonText) {
        var visible = switch (buttonText) {
            case "ADD TO CART" -> productDetailPage.isAddToCartVisible();
            case "REMOVE"      -> productDetailPage.isRemoveVisible();
            default            -> throw new IllegalArgumentException(
                    "Botão não mapeado: '%s'".formatted(buttonText));
        };
        Assertions.assertTrue(visible, "Botão '%s' não está visível".formatted(buttonText));
    }

    @Entao("o preço do produto deve ser exibido")
    public void validarPreco() {
        var price = productDetailPage.getPrice();
        Assertions.assertFalse(price.isBlank(), "Preço do produto está vazio");
    }

    @Quando("o usuário adiciona o produto ao carrinho")
    public void adicionarAoCarrinho() {
        productDetailPage.tapAddToCart();
        Assertions.assertTrue(productDetailPage.isRemoveVisible(),
                "Produto não foi adicionado (botão REMOVE ausente)");
    }

    @Quando("o usuário volta para a tela de produtos")
    public void voltarParaProdutos() {
        productsPage = productDetailPage.tapBackToProducts();
    }

    @Quando("o usuário toca no ícone do carrinho")
    public void tocarIconeCarrinho() {
        cartPage = productsPage.tapCartIcon();
    }

    @Entao("a tela do carrinho deve ser exibida")
    public void validarTelaCarrinho() {
        Assertions.assertTrue(cartPage.isOnCartPage(), "Tela do carrinho não foi exibida");
    }

    @Entao("o carrinho deve conter {int} item")
    public void validarQuantidadeItens(int expected) {
        var actual = cartPage.getItemCount();
        Assertions.assertEquals(expected, actual,
                "Carrinho deveria ter %d item(s), mas tem %d".formatted(expected, actual));
    }

    @Quando("o usuário toca em {string}")
    public void tocarEmBotao(String buttonText) {
        switch (buttonText) {
            case "BACK TO PRODUCTS" -> productsPage = productDetailPage.tapBackToProducts();
            default                 -> throw new IllegalArgumentException(
                    "Botão de navegação não mapeado: '%s'".formatted(buttonText));
        }
    }
}
