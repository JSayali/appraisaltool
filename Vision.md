# Introduction #

Vision of Appraisal Tool, in czech lang (original documentation).

# Vize #

Úkolem bude vytvořit server aplikaci, která umožní evidenci důkazů při přípravě na certifikaci CMMI. Uživatelské rozhraní bude webové

## Klient ##

  * UI bude podoobné,. jako aapraisal asistent
  * Tisk reportu do ODT.

## Server ##

  * Aplikační logika bude implementována v Spring 3.0
  * Data se budou ukládat v databázi přes JPA

## CMMI ##

  * Metodika SCAMPI 1.2 (CMMI for Development 1.2 Mat. Level 2-5)
  * S možností přidat/nadefinovat další
  * Item based: Každá zaznamenaná evidence, je opatřena poznámkou, a propojena s modelem (tj. jeden důkaz je možno přiřadit k více místům modelu).
  * Procesní oblasti s možnosti přizpůsobení

## Další poznámky ##

  * Víceuživatelské
  * Role leader a member, každý appraisal má svůj tým
  * Možnost ukládat URI na důkazy
  * Pro více firem
  * V každé firmě možno provádět appraisal na více projektech.
  * Přehledové grafy ukazující postup appraisalu, podobně jako v AA.