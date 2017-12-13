## ROOM

### OBS: A implementação está na branch room
----------

O Room tira da mão do programador as responsabilidades de manter as tabelas consistentes ( evitando que o programador escreva o código SQL na mão para sua criação ) e de atualizar-las à medida que o banco vai evoluíndo ( no SQLite o programador que escrevia o update de uma versão para outra, o que poderia deixar o código de update gigantesco dependendo da quantidade de versões ), que por sí só já são motivos bons o suficiente para ele passar a ser adotado pois estas tarefas são muito suscetíveis a erro.

Além disso, no Room é muito fácil criar entidades. Não posso opinar ainda sobre relacionamentos pois não tive oportunidade de utilizá-los neste projeto, mas com certeza irei verificar. 

Não tive dificuldades em passar o banco de SQLite para Room.

Os únicos pontos que tenho a destacar são:

1. O Decorator Query do Room parace mal otimizado pois o "SELECT * FROM table" demora muito mais que no SQLite, o que não faz muito sentido.
2. A interface do Provider não é compatível com o Room, muita complicação(herdada) para funções com implementações mais "diretas"(sem utilizar cursores). Creio que eles vão resolver isso nas próximas atualizações.


-------
## Live Data

Sobre o Live Data não tenho muito o que falar, apenas que ela é uma feature muito útil. Creio que ela será bem difundida na comunidade.
Não achei difícil o seu uso, só tive algumas dificuldades conceituais, mas quando consegui entender, desenvolvi facilmente.

Link para a demonstração do Live Data funcionando no app: https://www.dropbox.com/s/8yd4slmvg1hj1bz/Android%20Emulator%20-%204_WVGA_Nexus_S_Edited_API_24_5554%2010_12_2017%2023_56_18.mp4?dl=0



------
### EXTRA - Testes
Outro ponto a destacar: Tive que mudar o código de testes unitários do banco de dados. Eles ficaram mais enxutos(em questão de quantidade de código) com o Room ( e não foi preciso usar o PowerMockito) .

Por exemplo o teste de INSERT:

```java
	@Test
    public void isInsertingAItemProperly() throws Exception {
        ItemFeedDao itemFeedDao = mock(ItemFeedDao.class);
        ItemFeed itemFeed = new ItemFeed("Aliens","somesitelink.com","05/09/1995","lorem ipsum","somedownloadsite.com","someuri.com",1);
        //mocking methods and classes
        when(itemFeedDao.insert(any(ItemFeed.class))).thenReturn(1L);

        long insertResult = itemFeedDao.insert(itemFeed);
        assertEquals(insertResult,1L);
    }
```