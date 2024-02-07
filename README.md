# Pokedex Simplificada

Este projeto visa proporcionar uma experiência prática no consumo de API's com [Ktor](https://ktor.io/docs/getting-started-ktor-client.html) e armazenamento de dados com [ObjectBox](https://objectbox.io/). A ideia é construir uma Pokedex simples, que obtenha dados da [PokeApi](https://pokeapi.co/), exibindo uma lista de pokémons ao usuário e permitindo a montagem de equipes com os pokémons disponíveis.

## Pré-requisitos

- [Android Studio](https://developer.android.com/studio?gclid=Cj0KCQiAn-2tBhDVARIsAGmStVn0ZRoVaj-yzX9n2mn51-iL_fKmhvQv8iyFdjaUMuV_gyMgxlYToZoaAmQSEALw_wcB&gclsrc=aw.ds&hl=pt-br)
- Conhecimento básico em [Kotlin](https://kotlinlang.org/) e Android Studio

## Funcionalidades Principais

A Pokedex deve ser capaz de:

- Exibir uma lista de Pokémons contendo:
  - Nome
  - Imagem
  - Número na Pokedex
  - Tipos
- Ao clicar em um pokémon, abrir uma nova tela com os dados mencionados anteriormente, juntamente com uma descrição do pokémon e um link para possíveis evoluções.
- Permitir a busca por nome na tela de listagem de pokémons.
- Possuir uma tela de criação de times, onde o usuário pode definir um nome e escolher os pokémons que farão parte do time.

## Implementação da Pokedex

### 1. Interface

Para implementar a interface deste projeto utilize o Jetpack Compose.

### 2. Estrutura

Os dados necessários para montar as Interfaces devem vir dos Serviços, que por sua vez devem adquirir esses dados de Repositórios, seguindo uma arquitetura de [3 camadas](https://www.ibm.com/br-pt/topics/three-tier-architecture).

## Testando a Pokedex

Execute o aplicativo em um emulador ou dispositivo Android e teste todas as funcionalidades da Pokedex para garantir seu funcionamento conforme o esperado.

## Considerações Finais

Este projeto serve como uma introdução ao consumo e armazenamento de dados. Sinta-se à vontade para adicionar recursos adicionais ou melhorias à Pokedex conforme desejar. A exploração e expansão do projeto são incentivadas para aprofundar seu aprendizado.
