
# A TextTiling Based Approach to Topic Boundary Detection in Meetings
  - Objetivo: Adaptar o algorítimo TextTiling ao contexto das reuniões
  - Não utiliza atas, mas extrai o texto dos audios das reuniões.
  - Não aproveita as frases 'pistas' para identificar finais dos segmentos, mas cita um trabalho que aproveita
  - Utiliza Janelas, as quais chama de 'candidate topic boundaries'. Usa intervalos de tempo e frases 'pistas' para identificar esses candidatos.
  - Diferentemente das Janelas deslizantes, essas janelas não são sobrepostas.
  - Usa 'Cosine Similarity' para comparar pares de janelas adjacentes.
  - Encontra os pontos de corte calculando subtraindo o desvio padrão da média de todos os vales. "To do so we first compute a cut–off value (as in [1]) by subtracting the standard deviation from the mean of all the depth scores."
  - Usa 3 conjuntos de reuniões (CMU-2, CMU-3 e SRI-1). Com 4, 5 e 5 reuniões cada, totalizando 14 reuniões. Em média, cada reunião dura 15 mim e 5 assuntos.
  - Durante as reuniões, um anotador humano marcava o tempo em que os participantes mudavam de assunto.
  - A algoritmo encontra os limites (boudaries) dos segumentos. Um limite é o ponto onde os participantes mudaram de assunto
  - Usa o número limites automáticos que casam com os limites marcados pelos anotadores para avaliar o agoritmo proposto.
  - Usa cross validation para avaliar - Obtiveram 0.85 de precisão média, recall 0.59 e f-measure 0.67.

# Statistical Models for Text Segmentation 

  - Usa dados de transmissões de Rádio e TV.


		+-------------------------------------------------------------------------------------------------------+
		|  this research was inspired by a problem in                                                           |
		| information retrieval: given a large unpartitioned collection of expository text (such as a           |
		| year’s worth of newspaper articles strung together) and a user’s query, return a collection           |
		| of coherent segments matching the query.                                                              |
		+-------------------------------------------------------------------------------------------------------+

		+-------------------------------------------------------------------------------------------------------+
		| Though we have equated topics with documents, real-world documents often consist of a 				| 
		| *subtle progression of topics*. Since our algorithm learns to identify boundaries by inspecting		| 
		| the style and content of the surrounding text, it could be used to identify these finer divisions		| 
		| within a document. However, this paper focuses exclusively on the task of detecting where				| 
		| one document ends and another begins.																	| 
		+-------------------------------------------------------------------------------------------------------+

		'________________
		/ The TextTiling \______________________________________________________________________________________
		|                                                                                                       |
		| The TextTiling algorithm, introduced by Hearst (1994), is a simple, domain-independent                |
		| technique that assigns a score to each topic boundary candidate (inter-sentence position)             |
		| based on a cosine similarity measure between chunks of words appearing to the left and right          |
		| of the candidate. *Topic boundaries are placed at the locations of valleys in this measure,           |
		| and are then *adjusted* to coincide with known paragraph boundaries*.                                 |
		|_______________________________________________________________________________________________________|
		+-------------------------------------------------------------------------------------------------------+
		|                                                                                                       
		|                                                                                                       
		  1. Recebe uma lista cadidados a limite entre tótipicos, frequentemente finais de sentença.
		  2. Atribui um _score_ a cada candidato, o qual é calculado por meio da analise do texto que o circunda
                                                                                                                 /
                                                                                                                /
		+-------------------------------------------------------------------------------------------------------+
		



		'___________________
		/ Cue-word features \___________________________________________________________________________________
		|                                                                                                       |
		| In certain domains, selected words can often act as cues, indicating the presence of a nearby         |
		| boundary                                                                                              |
		|_______________________________________________________________________________________________________|

        - Their cue phrases are drawn
          from an empirically selected list of words (Hirschberg & Litman, 1993), while our approach
          allows all of the words in a fixed vocabulary to participate as candidate features.


		'________________________________________________		
		/ TDT pilot study (Topic Detection and Tracking) \______________________________________________________    
		|                                                                                                       |
		| research program to assess                                                                            |
		| and advance the state of the art in technologies for managing large amounts of information            |
		| in the form of newswire, TV and radio broadcasts. The study was organized around three                |
		| specific tasks: segmenting sources into stories, detecting the occurrence of new events,              |
		| and tracking labeled events in the data stream.                                                       |
		|                                                                                                       |
		| Yamron et al. (1998) developed an approach to segmentation                                            |
		| that treats a story as an instance of some underlying topic, and models an unbroken text              |
		| stream as an unlabeled sequence of topics using a hidden Markov model. In this approach,              |
		| finding story boundaries is equivalent to finding topic transitions                                   |
		|_______________________________________________________________________________________________________|
	    [O que exatamente é TDT? Estudo, documentos, domínio]


		'_________________________________________________
		/A feature-based approach using exponential models\_____________________________________________________
		|                                                                                                       |
		| Our approach to the segmentation problem is based on the statistical framework of feature             |
		| selection for random fields and exponential models                                                    |
		| The idea is to construct a model that                                                                 |
		| assigns a probability to the end of every sentence—the probability that that there exists a           |
		| boundary between that sentence and the next                                                           |
		|_______________________________________________________________________________________________________|


		'_____________
		/ Conclusions \_________________________________________________________________________________________
		|                                                                                                       |
		| We have presented and evaluated a new statistical approach to segmenting unpartitioned text           |
		| into coherent fragments. This approach uses feature selection to collect a set of informative         |
		| features into a model which can be used to predict where boundaries occur in text.                    |
		|_______________________________________________________________________________________________________|



>	Avaliação
	[Reference Segementation]
	[Seria TDT um conjunto de documentos já segmetados?]
	[Faz comparações com TextTiling]


# A Critique and Improvement of an Evaluation Metric for Text Segmentation

   - Aponta vários problemas na avaliação mais tradicional P_k, como a demaziada penaliação dos falsos negativos e a desconsideração de _near misses_ (quando o segmento fica próximo do esperado)

   - Propõe uma nova métrica de alvaliação chamada _WindowDiff_ 
     - A ideia é mover uma janela pelo texto e penalizar o algorítmo sempre que o número de separações (proposto pelo algoritmo) não conicidir com o número de separações (reais) para aquela janela de texto. 

		'_______________________________________________________________________________________________________
		|                                                                                                       |
		| This                                                                                                  |
		| new metric—called WindowDiff—moves a fixed-sized window across the text and penalizes the             |
		| algorithm whenever the number of boundaries within the window does not match the true number          |
		| of boundaries for that window of text.                                                                |
		|_______________________________________________________________________________________________________|


   [Text segmentation is the task of determining the positions at which topics change in a stream of text]

   - Há autores que usam aprendizado de máquina para detectar palavras-pista e outros as selecionam manualemnte
		
		[ others use machine learning techniques to detect cue
		words, or hand-selected cue words to detect segment boundaries ]

   

>	"passage retrieval, a subproblem of information retrieval "


   - Há duas principais dificuldades na avaliação de algoritmos de segmentação de texto.
      1. Avaliadores humanos nem concordam onde as deve haver uma separação no texto. Desse modo é difícil escolher uma segmentação de referência para comparações. 
	     Algumas avaliações contornan esse problema detectando segmentações em conjuntos de documentos concatenados
      2. Para diferentes aplicações para segmentação textual há diferentes tipos de erros a conciderar.
	     Por exemplo, no contexo de _informatin retrieval_ é tolerável certa impressisão de algumas sentenças na segmentação, o que não é aceitável para segmentação de notícias, onde a precisão é crucial.
	         **Por isso, alguns pesquisadores preferem avaliar não o algoritmo, mas ao ivés disso, seu impacto na aplicação final.**


>	**Segmentação é identificar divisiões entre unidades de informação sucessivas (Beeferman, Berger, and Lafferty (1997))**


   - Fornece a probabilidade de duas sentenças extraídas randomicamente de conjunto de documentos pertencerem ao mesmo documento.


# Efficient Linear Text Segmentation Based on Information Retrieval Techniques

		[ The task of linear text segmentation is to split a large text
		document into shorter fragments, usually blocks of consecu-
		tive sentences. ]

		'_______________________________________________________________
		|                                                               | 
		| Topics are                                                    | 
		| detected based on the assumption that the distribution of     |  
		| words can be used as indicator for the coherence of text      |     
		| fragments. This property of text is called lexical coherence, |
		| where a change in vocabulary correlates with a change of      |
		| topics.                                                       | 
		|_______________________________________________________________|
		
		

# Linear Discourse Segmentation of Multi-Party Meetings Based on Local and Global Information

		'_____________
		/ Introdução  \_____________________________________________________
		|                                                                   |
		| M                                                                 | 
		|  EETINGS are important activities for businesses,                 | 
		| managemntes and organizations in where people spend               | 
		| plenty of their daily time. Organazing these data is tradionally  | 
		| done by human operators.  The growing number of daily meet-       | 
		| ings makes it very expensive or impossible to maintain and        | 
		| structure all the information exchanged in them. Accordingly      | 
		| automatically understanding and structuring meetings has          | 
		| attracted a great deal of attention in the last decade. However,  | 
		| understanding a meeting thoroughly (in a way that all the         | 
		| related questions can be answered and the minutes can be ex-      | 
		| tracted automatically) is a challenging task for computers, and   | 
		| hasn’t been achieved yet.		                                    | 
		|___________________________________________________________________|
	



_______________________________________________________________________________________________________
*******************************************************************************************************

# Introdução


	[A Critique and Improvement of an Evaluation Metric for Text Segmentation]

		'____________________________________________________________________________________
		|                                                                                    | 
		| Interest in automatic text segmentation has blossomed over the last                |
		| few years, with applications ranging from information retrieval to text summariza- |
		| tion to story segmentation of video feeds.                                         |
		|____________________________________________________________________________________|

		'_______________________________________________________________
		|                                                               | 
		| Text segmentation issues are also important                   |
		| for passage retrieval, a subproblem of information retrieval  |
		|_______________________________________________________________|

		More recently,
		a great deal of interest has arisen in using automatic segmentation for the detection
		of topic and story boundaries in news feeds
	


________________________________________________________________________________________________________

	[Automatic Text Decomposition Using Text Segments and Text Themes]

		'________________________________
		|                                | 
		| With the advent of full-text   |
		| document processing, the       |
		| interest in manipulating       |  
		| text passages rather than only |
		| full-text                      |
		| items has continued to grow.   |
		|________________________________|


		In studying the structure of written texts, we are inter-
		ested in identifying text pieces exhibiting internal con-
		sistency that can be distinguished from the remainder
		of the surrounding text. 



________________________________________________________________________________________________________

	[Statistical Models for Text Segmentation]


		'____________________________________________________________________________________________
		|  construct a                                                                               |
		| system which, when given a stream of text, identifies locations where the topic changes.   |
		|____________________________________________________________________________________________|


		real-world documents often consist of a
		subtle progression of topics

		'____________________________________________________________________________________________
		|                                                                                            | 
		| Several proposed approaches to the text segmentation problem rely on some measure of       |
		| the difference in word usage on the two sides of a potential boundary: a large difference  |
		| in word usage is a positive indicator for a boundary, and a small difference is a negative |
		| indicator.                                                                                 |
		|____________________________________________________________________________________________| 





________________________________________________________________________________________________________

	[Topic segmentation for textual document written in Arabic language]

		
		'______________________________________________________________
        / This technique is used to improve the access to information. \_______________________________________________________                    
		|                                                                                                                      | 
		|  This technique is used to improve the access to information.                                                        | 
		| For example, in information retrieval, short relevant text segments that directly correspond to the user’s query can | 
		| be returned instead of long documents. For text summarizing, a better summary can be obtained from topically seg-    | 
		| mented documents.                                                                                                    | 
		|______________________________________________________________________________________________________________________| 






_______________________________________________________________________________________________________
*******************************************************************************************************

# Funcionamento do TextTiling

		'______________________________________________________________________________________________
		|                                                                                              | 
		| The TextTiling algorithm, introduced by Hearst (1994), is a simple, domain-independent       | 
		| technique that assigns a score to each topic boundary candidate (inter-sentence position)    | 
		| based on a cosine similarity measure between chunks of words appearing to the left and right | 
		| of the candidate. Topic boundaries are placed at the locations of valleys in this measure,   | 
		| and are then adjusted to coincide with known paragraph boundaries.                           | 
		|______________________________________________________________________________________________| 

	[Statistical Models for Text Segmentation]



	* usa uma janela deslisante para computar as similaridades entre blocos de texto adjacente baseado em seus vetores de frequêcia.
	* 


	3 Fases:
		* preprocessamento
		1. construção dos blocos usando janela deslisante; 
		2. Cálculo das similaridades usando cosine;
		3. Os limites são determinados pelas mudanças na sequência dos scores de similaridades;


	
		
	- Baixa complexidade e baixa acurácia
	-


	--> Cada posição _i_ entre sentenças adjacentes é candidata a quebra de sentença. Para posição candidata são construídos 2 blocos. Um contendo sentenças que precedem a posição e outro com as que o sucedem. O tamanho desses blocos é um parâmetro a ser fonecido a algoritmo e determina o tamanho mínimode uma segmento.
	As similaridades entre os blocos são calculadas pela análise das frequencias das palavras que a compõe.



	(([Kern] leva em conta ainda mais uma medida chamada de _inner similarty_ a qual é a similaridade entre as sentenças de cada bloco, em seguida))

	((No trabalho de [Kern] outra medida é calculada, a inner similarty, ))
	As similaridades entre as sentenças são calculadas pela análise das frequencias das palavras que a compõe. A similaridade entre as senteças de cada bloco é chamada de _inner similarity_

________________________________________________________________________________________________________




# Funcionamento do C99

	4 Fases:
		* Não faz preprocessamento (originalmente) ???????????????
		1. Construção de um dicionário de frequências (vetor que contem a frequencia de cada palavra)
		2. Construção de uma matrix de similaridades (usa cosine)
		3. Cálculo da matrix de ranking
		4. Identificar os limites usando Reynar's maximisation algorithm

	- Transforma a matrix de similaridade em uma matrix de ranking e Usa _divisive clusterim_ para encontrar os melhores segmentos	[Kern]

	- Exige maior coplexidade computacional pq usa matriz de similaridades de todas as sentenças do documento


	

________________________________________________________________________________________________________


********************************************************************************************************
***********************************************MEDIDAS**************************************************
********************************************************************************************************

________________________________________________________________________________________________________

# Medida Pk

	- Medidas como precisão e recall podem não reprentar bem a performance do algoritmo, uma vez que desconsideram limites próximos ao esperado.
	- Calcula a discordância entre uma segmentação automática e uma real.
		- Uma discordância ocorre quando um par de palavras é colocado em segmentos diferentes, onde a distancia entre as palavras _k_ é calculado como a metada das comprimentos médios dos segentos reais (referência)
	- Quando aplicado a todos os termos produzidos, Pk pode ser interpretado como a probablilidade de erro geral [Kern]
	- [Kern] inclui o Pk por ser a mais utilizada, mas tambem inclui WindowDiff para referencias futuras



# Medida WindowDiff

	- 

	

________________________________________________________________________________________________________



________________________________________________________________________________________________________


# Trabalhos que tratam de reuniões faladas

	- Discurse Segmentation of Multi-Party Conversation
	- Linear Discurse Segmentation of Multi-Party Meetings Based on Local and Global Information
	- 
	
##		Elementos da fala
			- pausas
			- troca de locutor
			- picos de acentuação (acentuação da fala)
			- 





________________________________________________________________________________________________________


________________________________________________________________________________________________________

# Motivações para Segmentação

	- Aprimoração de recuperação de informação
	- Aprimoração de sumarização
	- Facilidade na navegação pelo documento
	- 


________________________________________________________________________________________________________














Pevzner,
title={A critique and improvement of an evaluation metric for text segmentation},

Banerjee,
title={A TextTiling based approach to topic boundary detection in meetings},

Salton,
title={Automatic text decomposition using text segments and text themes},

Kern,
title={Efficient linear text segmentation based on information retrieval techniques},

# Criou o Pk ???
Beeferman,
title="Statistical Models for Text Segmentation",


CHAIBI20,
title = "Topic Segmentation for Textual Document Written in Arabic Language",

Bokaei,
 title = {Linear Discourse Segmentation of Multi-party Meetings Based on Local and Global Information},

Galley,
 title = {Discourse Segmentation of Multi-party Conversation},


Hearst,
 title = {Multi-paragraph Segmentation of Expository Text},
 
