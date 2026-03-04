package com.automation.steps;

import com.automation.pages.LoginPage;
import com.automation.pages.ProductsPage;
import io.cucumber.java.pt.*;
import org.junit.jupiter.api.Assertions;

/** Step definitions para o feature de Login. */
public class LoginSteps {

    private LoginPage loginPage;
    private ProductsPage productsPage;

    @Dado("o aplicativo está aberto na tela de login")
    public void appAbertaNaTelaDeLogin() {
        loginPage = new LoginPage();
        Assertions.assertTrue(loginPage.isOnLoginPage(), "Tela de login não está visível");
    }

    @Quando("o usuário insere o e-mail {string}")
    public void inserirEmail(String email) {
        loginPage.enterUsername(email);
    }

    @Quando("o usuário insere a senha {string}")
    public void inserirSenha(String password) {
        loginPage.enterPassword(password);
    }

    @Quando("o usuário toca no botão de login")
    public void tocarBotaoLogin() {
        productsPage = loginPage.tapLogin();
    }

    @Entao("o usuário deve ser redirecionado para a tela de Produtos")
    public void validarTelaDeProdutos() {
        Assertions.assertTrue(productsPage.isOnProductsPage(),
                "Tela de Produtos não foi exibida após login");
    }

    @Entao("o título {string} deve estar visível")
    public void validarTitulo(String expected) {
        var actual = productsPage.getPageTitle();
        Assertions.assertEquals(expected, actual,
                "Título esperado: '%s' | Atual: '%s'".formatted(expected, actual));
    }

    @Entao("uma mensagem de erro deve ser exibida")
    public void validarErroVisivel() {
        Assertions.assertTrue(loginPage.isErrorVisible(), "Mensagem de erro não está visível");
    }

    @Entao("a mensagem de erro deve conter {string}")
    public void validarConteudoErro(String expected) {
        var actual = loginPage.getErrorMessage();
        Assertions.assertTrue(actual.contains(expected),
                "Mensagem esperada conter: '%s' | Mensagem atual: '%s'".formatted(expected, actual));
    }

    @Quando("o usuário realiza o logout")
    public void realizarLogout() {
        loginPage = productsPage.doLogout();
    }

    @Entao("o usuário deve estar na tela de login")
    public void validarTelaDeLogin() {
        Assertions.assertTrue(loginPage.isOnLoginPage(), "Usuário não está na tela de login");
    }
}
