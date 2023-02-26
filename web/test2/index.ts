// import { Modal } from 'bootstrap';

// compile : npx tsc index.ts

function createInput(name: string, placeholder: string, value: string, parent, classe: string, title: string): void {
    const tmp2 = document.createElement('input');
    tmp2.setAttribute("type", "text");
    tmp2.setAttribute("name", name);
    tmp2.setAttribute("placeholder", placeholder);
    tmp2.setAttribute("value", value);
    tmp2.setAttribute("class", "case " + classe);
    tmp2.setAttribute("id", name);
    if (title) {
        tmp2.setAttribute("title", title);
    }
    parent.appendChild(tmp2);
}

function construitCases(x: string, y: string, recalculResultat:boolean): void {

    const elt = document.getElementById('doc');

    const tailleX = x.length;
    const tailleY = y.length;

    if (elt != null) {

        let newEltx = document.getElementById('xValeur');
        newEltx.textContent = '';

        for (let i = tailleX - 1; i >= 0; i--) {
            const tmp = document.createElement('div');
            newEltx.appendChild(tmp);

            const name = "x" + (i + 1);
            createInput(name, name, "" + x[tailleX - 1 - i], tmp, 'case-x', name);
        }

        const newElty = document.getElementById('yValeur');
        newElty.textContent = '';

        for (let i = tailleY - 1; i >= 0; i--) {
            const tmp = document.createElement('div');
            newElty.appendChild(tmp);

            const name = "y" + (i + 1);
            createInput(name, name, "" + y[tailleY - 1 - i], tmp, 'case-y', name);
        }

        // retenues

        const eltRetenues = document.getElementById('retenues');
        eltRetenues.textContent = '';

        for (let i = x.length - 1 + y.length - 1; i >= 0; i--) {
            const name = "r" + (i + 1);
            createInput(name, name, "0", eltRetenues, 'case-retenues', name);
        }

        // valeurs intermediaires

        const eltValInterm = document.getElementById('valeursIntermediaires');
        eltValInterm.textContent = '';

        for (let j = 0; j < tailleY; j++) {

            const tmp = document.createElement('div');
            tmp.setAttribute("class", "d-flex justify-content-end")
            eltValInterm.appendChild(tmp);

            for (let i = tailleX - 1; i >= 0; i--) {

                const name = "x" + (i + 1) + "*y" + (j + 1);

                createInput(name, name, "", tmp, 'case-intermediaire', name);

            }

            for (let k = 0; k < j; k++) {
                const tmp2 = document.createElement('div');
                tmp2.textContent = '.';
                tmp2.setAttribute("style", "width:50px;");
                tmp2.setAttribute("class", "text-center");
                tmp.appendChild(tmp2);
            }


        }

        // résultat

        const eltResultat = document.getElementById('resultat');
        eltResultat.textContent = '';

        for (let i = x.length - 1 + y.length - 1; i >= 0; i--) {
            const name = "z" + (i + 1);
            createInput(name, name, "", eltResultat, 'case-resultat', name);
        }

        recalcul(recalculResultat);

    }
}

function construitCasesFact(n: string, recalculResultat:boolean): void {
    const size=n.length;
    let x=Array(size+1).join('0');
    let y=Array(Math.floor(size/2)+1).join('0');
    construitCases(x,y,recalculResultat);

    for(let i=0;i<x.length;i++){
        let elt=document.getElementById('x'+(i+1));
        if(elt){
            (elt as HTMLInputElement).value='';
        }
    }

    for(let i=0;i<y.length;i++){
        let elt=document.getElementById('y'+(i+1));
        if(elt){
            (elt as HTMLInputElement).value='';
        }
    }

    for(let i=0;i<x.length+y.length+2;i++){
        let elt=document.getElementById('z'+(i+1));
        if(elt){
            const v=n.charCodeAt(size-i-1)-'0'.charCodeAt(0);
            (elt as HTMLInputElement).value=''+v;
        }
    }

    for(let i=0;i<x.length+y.length+2;i++){
        let elt=document.getElementById('z'+(i+1));
        if(elt){
            if(size-i-1>=0) {
                const v = n.charCodeAt(size - i - 1) - '0'.charCodeAt(0);
                (elt as HTMLInputElement).value = '' + v;
            } else {
                (elt as HTMLInputElement).value = '0';
            }
        }
    }
    recalcul(recalculResultat);
}

function recalcul(recalculResultat : boolean) {
    let newEltx = document.getElementById('xValeur');
    let newElty = document.getElementById('yValeur');
    if (newEltx && newElty) {
        const tailleX = newEltx.childNodes.length;
        const tailleY = newElty.childNodes.length;

        const valeursIntermedaire: number[][] = [];
        const reste: number[] = [];
        const resultat: number[] = [];
        const notUsed = -1;

        for (let j = 0; j < tailleY; j++) {


            for (let i = tailleX - 1; i >= 0; i--) {

                const name = "x" + (i + 1) + "*y" + (j + 1);

                const eltx = document.getElementsByName("x" + (i + 1));
                const elty = document.getElementsByName("y" + (j + 1));

                if (eltx && eltx.length > 0 && elty && elty.length > 0) {
                    const valx = (eltx[0] as HTMLInputElement).value;
                    const valy = (elty[0] as HTMLInputElement).value;
                    const eltInterm = document.getElementsByName(name);
                    const colonne = i + j;
                    const ligne = j;
                    while (valeursIntermedaire.length <= colonne) {
                        valeursIntermedaire.push([]);
                    }
                    const colonneTab = valeursIntermedaire[colonne];
                    while (colonneTab.length <= ligne) {
                        colonneTab.push(0);
                    }
                    while (reste.length <= colonne + 1) {
                        reste.push(0);
                    }

                    if (valx && valx.length > 0 && valy && valy.length > 0) {

                        const nx = parseInt(valx);
                        const ny = parseInt(valy);
                        const v=nx * ny;
                        colonneTab[ligne] = v;

                        if (eltInterm && eltInterm.length > 0) {
                            (eltInterm[0] as HTMLInputElement).value= ''+(v%10);
                        }

                    } else {
                        colonneTab[ligne] = notUsed;

                        if (eltInterm && eltInterm.length > 0) {
                            (eltInterm[0] as HTMLInputElement).value= '';
                        }
                    }

                }
            }
        }

        // calcul des valeurs de retenues et de resultats
        for (let i = 0; i < valeursIntermedaire.length; i++) {
            let val = 0;
            let fin = false;
            for (let j = 0; j < valeursIntermedaire[i].length; j++) {
                if (valeursIntermedaire[i][j] == notUsed) {
                    fin = true;
                    break;
                }
                val += valeursIntermedaire[i][j];
            }
            while (reste.length <= i + 1) {
                reste.push(notUsed);

            }
            if (reste[i] >= 0) {
                val += reste[i];
                while (i >= resultat.length) {
                    resultat.push(notUsed);
                }
            }
            if (!fin) {
                let v1 = val % 10;
                let v2 = Math.floor(val / 10);
                resultat[i] = v1;
                reste[i + 1] = v2;
            } else {
                resultat[i] = notUsed;
            }
        }

        // affichage des valeurs de retenues
        const eltReste = document.getElementById('retenues');
        if (eltReste) {
            for (let i = tailleX - 1 + tailleY - 1; i >= 0; i--) {
                const name = "r" + (i + 1);
                const eltRes = document.getElementsByName(name);
                if (eltRes && eltRes.length > 0) {
                    const v1 = reste[i];
                    (eltRes[0] as HTMLInputElement).value = '' + ((v1 != notUsed) ? v1 : "");
                }
            }
        }

        if(recalculResultat) {
            // affichage des valeurs résultat
            const eltResultat = document.getElementById('resultat');
            if (eltResultat) {
                for (let i = tailleX - 1 + tailleY - 1; i >= 0; i--) {
                    const name = "z" + (i + 1);
                    const eltRes = document.getElementsByName(name);
                    if (eltRes && eltRes.length > 0) {
                        const v1 = resultat[i];
                        (eltRes[0] as HTMLInputElement).value = '' + ((v1 != notUsed) ? v1 : '');
                    }
                }
            }
        }

        console.log("valeurs intermediaires:", valeursIntermedaire);
        console.log("reste:", reste);
        console.log("resultat:", resultat);
    }
}

// const buttonMulti = document.getElementById('multiOk');
const buttonMulti = document.getElementById('initMulti');
buttonMulti?.addEventListener('click', function handleClick(event) {
    let res = window.prompt("multiplication (x*y)");
    if (res) {
        res = res.trim();
        if (res.length > 0 && res.indexOf("*")>0) {
            let tab = res.split("*");
            if (tab && tab.length === 2) {
                let x = tab[0];
                let y = tab[1];
                x = x.trim();
                y = y.trim();
                if (x.length > 0 && y.length > 0) {
                    if (x.length < y.length) {
                        const tmp = y;
                        y = x;
                        x = tmp;
                    }
                    recalculResultat=true;
                    construitCases(x, y, recalculResultat)
                }
            }
        }
    }
    // const element = document.getElementById('dialogMulti') as HTMLElement;
    // if(element) {
    //     // (element as any).modal('hide')
    //     const modal = new Modal(element);
    //     modal.hide()
    // }
});

const buttonFact = document.getElementById('initFact');
buttonFact?.addEventListener('click', function handleClick(event) {
    // const element = document.getElementById('factModalLabel');
    // if(element) {
    //     const modal = new Modal(element);
    //     modal.hide()
    // }
    let res = window.prompt("factorisation (n=x*y)");
    if(res) {
        res = res.trim();
        if (res.length > 0) {
            recalculResultat=false;
            construitCasesFact(res, recalculResultat);
        }
    }
});


const buttonRecalcul = document.getElementById('recalcul');

buttonRecalcul?.addEventListener('click', function handleClick(event) {
    recalcul(recalculResultat);
});

let recalculResultat=true;

{
    let x = '';
    let y = '';

// x="23";
// y="5";

//x = "100";
//y = "10";

    x = "123";
    y = "45";

    construitCases(x, y, recalculResultat);

}

