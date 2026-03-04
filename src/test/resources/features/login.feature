# language: pt
@login
Funcionalidade: Login - Autenticação de Usuário

  Contexto:
    Dado o aplicativo está aberto na tela de login

  @smoke @regression
  Cenário: Login com credenciais válidas
    Quando o usuário insere o e-mail "standard_user"
    E o usuário insere a senha "secret_sauce"
    E o usuário toca no botão de login
    Então o usuário deve ser redirecionado para a tela de Produtos
    E o título "PRODUCTS" deve estar visível

  @regression
  Cenário: Login com senha incorreta
    Quando o usuário insere o e-mail "standard_user"
    E o usuário insere a senha "senhaerrada"
    E o usuário toca no botão de login
    Então uma mensagem de erro deve ser exibida
    E a mensagem de erro deve conter "Username and password do not match"

  @regression
  Cenário: Login com campos vazios
    Quando o usuário toca no botão de login
    Então uma mensagem de erro deve ser exibida
    E a mensagem de erro deve conter "Username is required"

  @smoke @regression
  Cenário: Logout após login bem-sucedido
    Quando o usuário insere o e-mail "standard_user"
    E o usuário insere a senha "secret_sauce"
    E o usuário toca no botão de login
    Então o usuário deve ser redirecionado para a tela de Produtos
    Quando o usuário realiza o logout
    Então o usuário deve estar na tela de login
