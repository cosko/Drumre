# Drumre
Movie recommend-er stranica koja uzima korisnikove podatke s Facebook-a i Twitter-a te
na temelju njih i dinamičkih podataka koje aktivno sakuplja, prikazuje preporučene filmove za korisnika. 

##Workflow

- uzeti dodijeljeni task (ako vam ništa nije dodjeljeno, pitati cosa) i kreirati novi branch iz master-a 
sa smislenim nazivom
- lupiti naredbe ovim redom: ```npm i```, ```npm run watch```
- you're good to go

##P.S.

- ne pisati inline css, zato koristimo tailwind :)
- ako dodajete nove js module, uključiti ih u ui/app.js (primjer - splide.js)
- ako radite neku ponavljajuću komponentu na stranici, kreirati ju u templates/fragments folderu i koristiti ju kao fragment
- ne mergati u master kod koji ne radi, niti koji ispisuje nešto u konzolu
- za ostale savjete i pitanja, pitati wayka ili cosa
