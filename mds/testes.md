## Testes unitários

Para essa categoria, realizamos os seguintes testes que são detalhados por classe/método a seguir:


#### Classe: XmlParserUnitTest

Nesta classe há um método chamado *isParserWorking* que como o nome sugere, verifica se o parser implementado em *XmlFeedAdapter* está funcionando corretamente, ou seja, se ele está retornando uma lista de *ItemFeed* dado um link de um podcast.

**Comentário**: Houveram algumas complicações na realização deste teste. O principal problema foi o fato de que a classe *XmlFeedAdapter* é estática e não é possível fazer o mock destas classes com *Mockito*. Tivemos então que utilizar o *PowerMockito* que basicamente é uma extensão do mockito e dentre suas features está o mock de classes estáticas. Resolvido o problema, pudemos mockar o comportamento do parser e verificar seu funcionamento.

----------------------

#### Classe: JobServiceUnitTest
Esta classe possui um método chamado *doesJobDownloadJobServiceWorked* que verifica se o método *onStartJob*, que inicia o JobService, está funcionando de acordo com o esperado, ou seja, se ela retorna **true**.

**Comentário**: Neste teste conseguimos utilizar apenas o *Mockito* para a realização dos testes pois o método da classe *DownloadAndPersistJob* que estamos testando não era estático. 

----------------------

#### Classe: DatabaseUnitTest
Esta classe testa funcionalidades relacionadas à base de dados. Há dois métodos nela: o *isInsertingAItemProperly* e o *isUpdatingAItemProperly*.
No primeiro, mockamos o comportamento correto de uma inserção no banco de dados. No segundo verificamos o comportamento do sucesso de uma atualização de um item no banco de dados.

**Comentário**: Nestes testes nos também usamos o *PowerMockito*, pois a classe Uri possui métodos estáticos que servem na realização dos testes citados.

----------------------

**Comentário sobre os testes de unidade**: Particularmente achei muito limitado os testes de unidade. Basicamente quase tudo é dependente da plataforma e você não pode utilizar "nada" dela nos testes. Penso que se fosse em um projeto de larga escala, faria sentido mas pra projetos pequenos como este, não dá pra fazer quase nada sem mockar.

----------------------

## Testes instrumentados

Para essa categoria, realizamos os seguintes testes que são detalhados por classe/método a seguir:


#### Classe: IntentTest
Esta classe verifica o comportamento dos Intents do sistema. Há um método chamado *itemDetailClickIntent* que verifica se o clique em um item na lista da *MainActivity* dispara o Intent corretamente para a view a ser aberta.

**Comentário**: Tive algums problemas na utilização do *intended* estava bugando muito com informações corretas. No fim, tive que "forçar" o match 
com um any() da classe esperada. Além disto, nossa tela inicial demora a carregar por causa do Service e o teste não espera ela carregar. Não achei nenhuma maneira fácil de resolver isto, tive que utilizar o *sleep*. Penso eu que uma feature tão básica desta já teria que estar disponível ao usuário sem forçar a barra com um sleep.

----------------------

#### Classe: JobSchedulerTest

Esta classe verifica se as funcionalidades da ActivitySettings realmente estão funcionando de acordo. Há um método chamado *isJobScheduled* que basicamente ele clica no botão para agendar o Job e verifica se ele realmente foi agendado. E o método *isJobCancelled* verifica se a funcionalidade de cancelamento está sendo realizada corretamente, basicamente ela agenda um Job e depois o cancela e verifica a existencia do mesmo no jobScheduler.

**Comentário**: Novamente tive que utilizar o sleep para forçar esperar a tela carregar. E também tivemos algumas dificuldades para pegar o *jobScheduler* da aplicação corretamente.

-----------

**Comentário sobre os testes instrumentados**: Eu achei a API meio bugadinha em relação a algumas tarefas simples. Creio que seja eu não tive muito contato com ela e esteja fazendo algumas besteiras, mas levei um tempinho para resolver essas broncas.

