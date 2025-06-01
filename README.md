# Sistema de Gerenciamento de Biblioteca

Este é um sistema de gerenciamento de biblioteca desenvolvido em Java, utilizando JavaFX para a interface gráfica. O projeto segue a estrutura padrão de aplicações Java com Maven.

## Como clonar o projeto

Para obter uma cópia do projeto em sua máquina, execute o comando abaixo no terminal:

```bash
git clone https://github.com/mmd-moradi/LibraryManagementSystem.git
```
## Como contribuir

Siga os passos abaixo para contribuir com este projeto:

1. **Faça um fork do repositório**  
   No GitHub, clique em "Fork" no canto superior direito da página do repositório.

2. **Clone o seu fork para sua máquina**  
   ```bash
   git clone https://github.com/seu-usuario/LibraryManagementSystem.git
   ```
   Substitua `seu-usuario` pelo seu nome de usuário do GitHub.

3. **Crie uma nova branch para sua contribuição**  
   ```bash
   git checkout -b nome-da-sua-branch
   ```

4. **Implemente suas alterações**  
   Faça as modificações desejadas no código.

5. **Adicione e faça commit das alterações**  
   ```bash
   git add .
   git commit -m "Descreva brevemente sua contribuição"
   ```

6. **Envie sua branch para o seu fork no GitHub**  
   ```bash
   git push origin nome-da-sua-branch
   ```

7. **Abra um Pull Request**  
   No GitHub, acesse seu fork, selecione a branch criada e clique em "Compare & pull request". Descreva suas alterações e envie o PR.

8. **Aguarde a revisão**  
   Suas alterações serão analisadas e, se aprovadas, serão integradas ao projeto principal.

Fique à vontade para contribuir com melhorias, correções de bugs ou novas funcionalidades!

## Como executar o projeto no NetBeans IDE

Siga os passos abaixo para rodar o projeto no NetBeans:

1. **Abra o NetBeans IDE**
2. No menu, clique em **Arquivo > Abrir Projeto...**
3. Navegue até a pasta onde o projeto está localizado (`LibraryManagementSystem`) e selecione-a.
4. Clique em **Abrir Projeto**.
5. Aguarde o NetBeans carregar as dependências do Maven.
6. Para executar o projeto, clique com o botão direito sobre o projeto na aba "Projetos" e selecione **Executar** (ou pressione `F6`).

### Requisitos
- NetBeans IDE (recomendado: versão 12 ou superior)
- JDK 11 ou superior
- Maven (gerenciado automaticamente pelo NetBeans)

### Observações
- Certifique-se de que o JDK está corretamente configurado no NetBeans.
- O projeto utiliza JavaFX. Caso necessário, configure o caminho do JavaFX nas propriedades do projeto.
- Se houver problemas com dependências, clique com o botão direito no projeto e selecione **Limpar e Construir**.

## Estrutura do Projeto
- `src/main/java`: Código-fonte principal
- `src/main/resources`: Recursos como arquivos FXML e CSS
- `src/test/java`: Testes automatizados
