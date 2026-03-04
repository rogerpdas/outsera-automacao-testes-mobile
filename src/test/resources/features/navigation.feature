# language: pt
@navigation @regression
Funcionalidade: Navigation - Navegação Entre Telas

  Contexto:
    Dado o usuário está autenticado com e-mail "standard_user" e senha "secret_sauce"

  @smoke
  Cenário: Navegar para detalhe do produto e validar elementos
    Quando o usuário está na tela de Produtos
    E o usuário toca no primeiro produto disponível
    Então a tela de detalhe do produto deve ser exibida
    E o botão "ADD TO CART" deve estar visível
    E o preço do produto deve ser exibido

  @regression
  Cenário: Adicionar produto ao carrinho e validar quantidade
    Quando o usuário está na tela de Produtos
    E o usuário toca no primeiro produto disponível
    E o usuário adiciona o produto ao carrinho
    E o usuário volta para a tela de produtos
    E o usuário toca no ícone do carrinho
    Então a tela do carrinho deve ser exibida
    E o carrinho deve conter 1 item

  @regression
  Cenário: Voltar para produtos a partir do detalhe
    Quando o usuário está na tela de Produtos
    E o usuário toca no primeiro produto disponível
    Então a tela de detalhe do produto deve ser exibida
    Quando o usuário toca em "BACK TO PRODUCTS"
    Então o usuário deve estar na tela de Produtos
