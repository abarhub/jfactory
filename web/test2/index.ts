// import { Modal } from 'bootstrap';

// compile : npx tsc index.ts

function createInput(name: string, placeholder: string, value: string, parent, classe: string): void {
    const tmp2 = document.createElement('input');
    tmp2.setAttribute("type", "text");
    tmp2.setAttribute("name", name);
    tmp2.setAttribute("placeholder", placeholder);
    tmp2.setAttribute("value", value);
    tmp2.setAttribute("class", "case " + classe);
    parent.appendChild(tmp2);
}

function construitCases(x: string, y: string): void {

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
            createInput(name, name, "" + x[tailleX - 1 - i], tmp, 'case-x');
        }

        const newElty = document.getElementById('yValeur');
        newElty.textContent = '';

        for (let i = tailleY - 1; i >= 0; i--) {
            const tmp = document.createElement('div');
            newElty.appendChild(tmp);

            const name = "y" + (i + 1);
            createInput(name, name, "" + y[tailleY - 1 - i], tmp, 'case-y');
        }

        // retenues

        const eltRetenues = document.getElementById('retenues');
        eltRetenues.textContent = '';

        for (let i = x.length - 1 + y.length - 1; i >= 0; i--) {
            const name = "r" + (i + 1);
            createInput(name, name, "0", eltRetenues, 'case-retenues');
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
                const nx = parseInt(x[tailleX - i - 1]);
                const ny = parseInt(y[tailleY - j - 1]);
                const v = nx * ny;
                const v1 = v % 10;
                const v2 = (v - v1) / 10;
                createInput(name, name, "", tmp, 'case-intermediaire');
                if (v2 > 0) {
                    const eltRet = document.getElementsByName('r' + (i + j + 2));
                    if (eltRet.length > 0) {
                        const v3 = (eltRet[0] as HTMLInputElement).value;
                        let n = parseInt(v3);
                        n += v2;
                    }
                }
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
            createInput(name, name, "", eltResultat, 'case-resultat');
        }

        recalcul();

    }
}

function recalcul() {
    let newEltx = document.getElementById('xValeur');
    let newElty = document.getElementById('yValeur');
    if (newEltx && newElty) {
        const tailleX = newEltx.childNodes.length;
        const tailleY = newElty.childNodes.length;

        const tab: number[] = [];
        const valeursIntermedaire: number[][] = [];
        const reste: number[] = [];
        const resultat: number[] = [];

        for (let j = 0; j < tailleY; j++) {


            for (let i = tailleX - 1; i >= 0; i--) {

                const name = "x" + (i + 1) + "*y" + (j + 1);

                const eltx=document.getElementsByName("x" + (i + 1));
                const elty=document.getElementsByName("y" + (j + 1));

                if(eltx&&eltx.length>0&&elty&&elty.length>0) {
                    const valx=(eltx[0] as HTMLInputElement).value;
                    const valy=(elty[0] as HTMLInputElement).value;
                    if(valx&&valx.length>0&&valy&&valy.length>0) {
                        const nx = parseInt(valx);
                        const ny = parseInt(valy);
                        const v = nx * ny;
                        const v1 = v % 10;
                        const v2 = (v - v1) / 10;
                        const eltInterm = document.getElementsByName(name);
                        const colonne = i + j;
                        const ligne = j;
                        if (eltInterm && eltInterm.length > 0) {
                            eltInterm[0].setAttribute('value', '' + v1);
                        }
                        while (valeursIntermedaire.length <= colonne) {
                            valeursIntermedaire.push([]);
                        }
                        const colonneTab = valeursIntermedaire[colonne];
                        while (colonneTab.length <= ligne) {
                            colonneTab.push(0);
                        }
                        colonneTab[ligne] = v;
                        while (reste.length <= colonne + 1) {
                            reste.push(0);
                        }

                        const pos = i + j;
                        while (pos >= tab.length) {
                            tab.push(0);
                        }
                        tab[pos] = tab[pos] + v1;

                    }

                }
            }
        }

        for (let i = 0; i < valeursIntermedaire.length; i++) {
            let val = 0;
            for (let j = 0; j < valeursIntermedaire[i].length; j++) {
                val += valeursIntermedaire[i][j];
            }
            while(reste.length<=i+1) {
                reste.push(0);

            }
            val += reste[i];
            while (i >= resultat.length) {
                resultat.push(0);
            }
            let v1=val%10;
            let v2=Math.floor(val/10);
            resultat[i] = v1;
            reste[i+1]=v2;
        }

        const eltReste = document.getElementById('retenues');
        if (eltReste) {
            for (let i = tailleX - 1 + tailleY - 1; i >= 0; i--) {
                const name = "r" + (i + 1);
                const eltRes = document.getElementsByName(name);
                if (eltRes && eltRes.length > 0) {
                    const v1=reste[i];
                    (eltRes[0] as HTMLInputElement).value= '' + v1;
                }
            }
        }

        const eltResultat = document.getElementById('resultat');
        if (eltResultat) {
            for (let i = tailleX - 1 + tailleY - 1; i >= 0; i--) {
                const name = "z" + (i + 1);
                const eltRes = document.getElementsByName(name);
                if (eltRes && eltRes.length > 0) {
                    const v1=resultat[i];
                    (eltRes[0] as HTMLInputElement).value= '' + v1;
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
    console.log('button Multi clicked');
    console.log(event);
    console.log(event.target);
    let res = window.prompt("multiplication (x*y)");
    if (res) {
        res = res.trim();
        if (res.length > 0 && res.indexOf("*")) {
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
                    construitCases(x, y)
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

const buttonFact = document.getElementById('factOk');
buttonFact?.addEventListener('click', function handleClick(event) {
    console.log('button Fact clicked');
    console.log(event);
    console.log(event.target);
    // const element = document.getElementById('factModalLabel');
    // if(element) {
    //     const modal = new Modal(element);
    //     modal.hide()
    // }
});


const buttonRecalcul = document.getElementById('recalcul');

buttonRecalcul?.addEventListener('click', function handleClick(event) {
    recalcul();
});

let x = '';
let y = '';

// x="23";
// y="5";

//x = "100";
//y = "10";

x = "123";
y = "45";

construitCases(x, y);


