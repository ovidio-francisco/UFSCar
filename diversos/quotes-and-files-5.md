





------------------------------------------------------------
------------------------------------------------------------

Aqui, os tópicos são definidos como distribuições de
probabilidade sobre um vocabulário fixo de palavras. Enquanto que os documentos, nada mais
do que bag-of-words, surgem da escolha aleatória das palavras pertencentes a uma distribuições
de tópicos.

------------------------------------------------------------
------------------------------------------------------------


file:///ext4Data/UFSCar/papers/Tópicos/Modelos probabilísticos de tópicos: desvendando o Latent Dirichlet Allocation



-----------------------------------------

"Morris, J. and G. Hirst. 1991. Lexical Cohesion Computed
by Thesaural Relations as an Indicator of the
Structure of Text. Computational linguistics, 17:1, 21
– 48."

https://arxiv.org/pdf/1204.0255.pdf
----------------------------


-----------------------


https://www.google.com.br/search?q=lda+alpha+parameter&oq=lda+alpha+parameter&aqs=chrome..69i57.8119j0j7&sourceid=chrome&ie=UTF-8
https://stats.stackexchange.com/questions/37405/natural-interpretation-for-lda-hyperparameters
https://datascience.stackexchange.com/questions/199/what-does-the-alpha-and-beta-hyperparameters-contribute-to-in-latent-dirichlet-a
https://www.thoughtvector.io/blog/lda-alpha-and-beta-parameters-the-intuition/


http://www.themacroscope.org/?page_id=391
http://www.themacroscope.org/?page_id=391
https://github.com/senderle/topic-modeling-tool




----------------------------------------------------


2.4.1 Silhueta

2.3.3 Seleção de Descritores para Agrupamento



file:///ext4Data/UFSCar/papers/dissertações/Ricardo Marcondes Marcacini.pdf



----------------------------------------------------

TF-IDF
Assim, o peso de cada termo t i para cada documento d j da coleção é calculado como
apresentado na Equação 3.3.
w ij
 tf
idf ij
 tf i,j  idf i


file:///ext4Data/UFSCar/papers/dissertações/Tese_Fabiano_Fernandes_dos_Santos_revisada.pdf


Essas abordagens de extração de tópicos também fornecem uma estratégia
de redução da dimensionalidade visando a construção de novas dimensões que representam
os principais tópicos ou assuntos identificados na coleção de documentos. Ainda, uma or-
ganização baseada em tópicos agrupa termos com mesmo significado em um mesmo tópico
(sinonı́mia) e permite que um mesmo termo ocorra em mais de um tópico caso ele possa
ter significado diferente em diferentes contextos (polissemia). Uma representação cons-
truı́da com os novos atributos extraı́dos é uma oportunidade de incorporar conhecimento
de domı́nio aos dados (Guyon and Elisseeff, 2003).

"
Atualmente, os modelos de
extração de tópicos probabilı́sticos como o Latent Dirichlet Allocation (LDA) são abor-
dagens amplamente aplicadas (Zhu et al., 2012), sendo referenciadas na grande maioria
dos trabalhos da literatura como sinônimos de modelos de extração de tópicos (Stey-
vers and Griffiths, 2007; O’Callaghan et al., 2015).
"

"O resultado do processo de extração de tópico é uma representação
documento-tópico que determinam um peso de cada tópico para cada documento e uma
representação termo-tópico. Esta última está relacionada com o modelo generativo que
foi escolhido, e pode representar uma probabilidade da ocorrência do termo quando um
tópico ocorre em um documento, a frequência esperada desse termo, ou mesmo um peso
estimado matematicamente que não pode ser “traduzido” com algum significado para o
contexto linguı́stico."

-----------------------------------------------------------------------


-----------------------------------------



file:///ext4Data/UFSCar/papers/dissertações/ThiagodePauloFaleiros_revisada.pdf


A extração de informação em dados textuais envolve diretamente a tentativa de se extrair
informações úteis em coleções de documentos textuais. O que se objetiva nessa abordagem
é a criação de uma representação estruturada das informações retiradas dessas coleções de
documentos (AGGARWAL; ZHAI, 2012). O modelo espaço vetorial é o modelo mais tradicional
para obter uma representação estruturada dos documentos.




----------------------------------------


3.2.1 Extração de Atributos

O processo de extração de atributos está relacionado à criação de um novo conjunto
de atributos, usando para tal uma função de mapeamento entre as representações posici-
onando os dados em uma dimensão latente de forma a representar informações que não
são capturadas com os atributos originais. O novo espaço produzido pode ser obtido pela
combinação linear ou não-linear dos atributos originais, por transformações que mapeiam
o documento em um espaço de conceitos, ou por técnicas probabilı́sticas como os modelos
generativos de extração de tópicos. Ainda que seleção de atributos seja computacional-
mente mais simples e produza resultados significativos, a construção de novos atributos
para coleções de textos se mostra relevante para tarefas em que a interpretabilidade é um
fator importante. Além disso, as técnicas de extração de atributos são bem sucedidas em
descobrir a estrutura latente da coleção de documentos (Shafiei et al., 2007; Hava et al.,
2013). Uma representação construı́da com novos atributos extraı́dos é uma oportunidade
de incorporar conhecimento de domı́nio aos dados (Guyon and Elisseeff, 2003; Shafiei
et al., 2007; Farahat and Kamel, 2011; Kalogeratos and Likas, 2012).


-- Fabiano





"The art of machine learning starts with the design of appropriate data representations.  Better performance is often achieved using features derived from the original input. Building a feature representation is an opportunity to incorporate domain knowledge into the data and can be very application specific. Nonetheless, there are a number of generic feature construction methods, including: clustering; basic linear transforms of the input variables (PCA/SVD, LDA); more sophisticated linear transforms like spectral transforms (Fourier, Hadamard), wavelet transforms or convolutions of kernels; and applying simple functions to subsets of variables, like products to create monomials"

file:///ext4Data/UFSCar/papers/2018/An introduction to variable and feature selection.pdf


-------------------------------------------------


"We use symmetric Dirichlet priors in the LDA estimation with
α = 50 / K and β =0.01, which are common settings in the
literature. Our experience shows that retrieval results are not very
sensitive to the values of these parameters."


"
Unlike the basic document representation, the LDA-based
document model is not limited to only the literal words in a
document, but instead describes a document with many other
related highly probable words from the topics of this document.
For example, for the query “buyout leverage”, the document
“AP900403-0219”, which talks about “Farley Unit Defaults On
Pepperell Buyout Loan”, is a relevant document. However, this
document focuses on the “buyout” part, and does not contain the
exact query term “leverage”, which makes this document rank
very low. Using the LDA-based representation, this document is
closely related to two topics that have strong connections with the
term “leverage”: one is the economic topic that is strongly
associated with this document because the document contains
many representative terms of this topic, such as “million”,
“company”, and “bankruptcy”; the other is the money market
topic which is closely connected to “bond”, also a very frequent
word in this document. In this way, the document is ranked
higher with the LDA-based document model. Having multiple
topics represent a document tends to give a clearer association
between words than the single topic model used in cluster-based
retrieval.
"



file:///ext4Data/UFSCar/papers/Extração de tópicos E Recuperação de Informação/Mais Recentes/LDA-Based Document Models for Ad-hoc Retrieval.pdf


-------------------------------------------------




"
When human
beings retrieve information, they use background knowledge to interpret and understand
the text and effectively “add in” words that may be missing. Ranking algorithms solely
based on matching the literal words that are present will fail to retrieve much relevant
information.
"

file:///ext4Data/UFSCar/papers/Extração de tópicos E Recuperação de Informação/TOPIC MODELS IN INFORMATION RETRIEVAL.pdf