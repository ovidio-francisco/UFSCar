
# Aplicação de técnicas de Mineração de Textos para Organização e Extração de Históricos de Decisões de Documentos de Reuniões


# Resumo

	

# Introdução
	
  - Popularização dos computadores e crescimento de arquivos digitais.
  - Custo para mantere documentos manualmente. Necessidade ferramentas para resgate de informações digitais.
  - Importância de processos de extração de conhecimento em coleções textuais.
  - Dificuldade em processar informações não estruturadas, volumosas e com texto irrelevante.
  - Produção de textos é popular e presente em muitos ambientes.
    - A tarefa de consulta manual é custosa e falha. 
  - Descrição das atas.
    - Conteúdo
    - Formato textual usado para consultas posteriores.
    - Usadas como registro das reuniões usadas como base para outras reuniões e decisões.
  - Departamento de computação como exemplo. [ex: credenciamento de docentes no programa]
    - O mesmo assunto pode ser mensionado várias vezes ao longo do tempo, podendo conter alterações. 
    - É desejavel saber quais decisões foram tomadas a respeito e o histórico do que já foi discutido.
  - Dificuldade em manter as atas e localizar assuntos discutidos divido a:
    - Volume de texto.
    - Falta de estruturação.
    - As consultas são feitas normalemente com apoio de ferramentas de busca por palavra chave e/ou memória dos usuários.
  - Técnicas de Mineração de Texto são empregradas a esse tipo de problema. Por exemplo:
    - Exemplos onde é empregada.
    - Técnicas que são utilizadas. 
  - Justificativa do trabalho 


	  
# Objetivos

  - O objetivo é desenvolver um sistema de consultas a conteúdos de atas de reuniões.
  - Justificativa e contribuição do trabalho.
  	- Testar se a aplicação de técnicas de MT nas atas ajuda a responder as consultas do usuário.
  - Estudar e Empregar tecnicas de MT e IR em sub-documentos das atas de reunião.
  	

# Funcionamento do sistema

  - Dividido em 2 módulos
    - Preparação e Manutenção --> Recebe uma coleção de documento e produz uma estrutura de dados
    - Consulta --> Recebe uma consulta e retorna um histórico de mensões (Segmentos de texto que contém o tópico)

  - Dado um conjunto de documentos, estes serão divididos em segmentos que contém um assunto predominante.
  - Cada segmento terá seu conteúdo identificado por meio de uma ferramenta de extração de tópicos.
	
  * O módulo de consulta busca os segmentos que contém o tópico de interesse do usuário.


  Dado um conjunto de n documentos D = (d1, d2, d3, ..., dn), cada documento i é dividido em m segmentos S = (s1i, s2i, s...mi). Cada segmento pode ser visto como um pseudo-documento. 
  
  
  Os segmentos e seus assuntos são representados por uma matriz documento-tópico que contém o peso de cada tópico para cada documento e uma matriz termo-tópico que representa a probabilidade de um termo ocorrer em um documento que contem um tópico.

  
=================================================================================================================================
  	
# Justificativa	
  	
  	- Testar se a alicação de técnicas de MT nas atas ajuda a responder às consultas do usuário.
  	- Entrega segmentos coerentes e relacionados à intensão do usuário.
  	- As técnicas de MT será testas e resultados serão reportados utilizando um 
  	- Reportar resultados das técnicas de MT aplicadas a um domínio com características particulares. [Docs concisos, palavras com baixa frequencia, sem parágrafos.]
  	- As abordagens desse trabalho podem ser aproveitadas em pesquisas semelhantes como notícias, audios transcritos, discursos, conversas ao telefone, reuniões em geral.
  	- 
  	
## Key words Vs Reconhecimento de tópicos 
  	
  	- O sistema fará buscas pelo tópico a ser pesquisado ao invés de palavras. 
  		- Ao pesquisar por uma palavra, esta pode estar presente em um segmento que não a tem como assunto central, mas apenas foi mensionada no segmento sem grade relevância. Nesse caso, o sistema pode atribuir uma menor relevâcia a esse segmento.
  	- O sistema será capaz reter textos que não contenham o tópico.
  		- Apenas o trecho que contém o tópico será retornado, evitando texto circundante caso não compartilhe o tópico.
	- Aprimora a negação pelo texto, principalmente para defícientes visuáis, onde o texto é lido.	
  	- O texto retornado é coeso e completo. 
	- Em Buscas por palavras chave, é retornado o documento completo, apenas com a palavras chave em destaque.
	
	Ao fazer buscas por tópicos, espera-se que o sistema dê resultados mais coerentes com a intensão do usuário.
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
