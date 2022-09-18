package ADTMatrix;

public class MatrixOps {

    // Menyalin matriks pada suatu matriks lain
    public Matrix copyMatrix (Matrix m){
        Matrix mOut = new Matrix(m.getRowLength(), m.getColLength());
        for (int i = 0; i <= m.getRowIdx(); i++){
            for (int j = 0; j <= m.getColIdx(); j++){
                mOut.setElmt(i, j, m.getElmt(i, j));
            }
        }
        return mOut;
    }

    // Mencari determinan dari sebuah matriks dengan Kofaktor
    public double detKof(Matrix m){
        double det = 0;
        if (!m.isSquare()){
            return m.MARK;
        }
        if (m.getRowLength() == 1)
        {
            return m.getElmt(0, 0);
        }
        else
        {
            for (int i = 0; i <= m.getColIdx(); i++)
            {
                Matrix mTemp = new Matrix(m.getRowLength() - 1, m.getColLength() - 1);
                for (int j = 1; j <= m.getRowIdx(); j++)
                {
                    for (int k = 0; k <= m.getColIdx(); k++)
                    {
                        double val = m.getElmt(j,k);
                        if (k < i)
                        {
                            mTemp.setElmt(j-1, k, val);

                        }
                        else if (k > i)
                        {
                            mTemp.setElmt(j-1, k-1, val);

                        }
                        
                    }
                    
                }
                
                det += Math.pow(-1, i) * m.getElmt(0, i) * detKof(mTemp);
            }
            return det;
        }
    }

    // Mencari determinan dengan cara OBE dan Kofaktor
    public double detObe (Matrix m){
        double det;
        int count;
        int swapCount = 0;
        double pembuatNol;
        double newVal;

        Matrix mTemp = new Matrix(m.getRowLength() - 1, m.getColLength() - 1);

        if (!m.isSquare()){
            return m.MARK;
        }
        if (m.getRowLength() == 1)
        {
            return m.getElmt(0, 0);
        } else {
            // Rekurens
            // Cek dulu apakah element pertama bernilai 0
            if (m.getElmt(0, 0) == 0){
                count = 0;
                while (m.getElmt(0, 0) == 0 && count < m.getRowLength()){
                    count++;
                }
                if ( count == m.getRowLength()){
                    return 0;
                } else {
                    m.swapRow(0, count);
                    swapCount++;
                }
            }

            // Membuat 0 semua baris lain pada kolom pertama
            // Mengurangi semua elemen pada baris 1 dengan k.elemen dari pada baris pertama
            for (int i = 1; i < m.getRowLength(); i++){
                // Jika sudah 0 maka dibiarkan saja
                if (m.getElmt(i, 0) != 0){
                    pembuatNol = m.getElmt(i, 0)/ m.getElmt(0, 0);
                    for (int j = 0; j < m.getColLength(); j++){
                        newVal = m.getElmt(i, j) - pembuatNol* m.getElmt(0, j);
                        m.setElmt(i, j, newVal);
                    }
                }
            }
            
            // memasukkan submatriks menjadi matriks baru
            for (int i = 1; i < m.getRowLength(); i++){
                for (int j = 1; j < m.getColLength(); j++){
                    mTemp.setElmt(i-1,j-1, (m.getElmt(i, j)));
                }
            }

            // setiap adanya pertukaran baris, nilai determinan dikali -1
            det = Math.pow(-1, swapCount) * m.getElmt(0,0) * detObe(mTemp);
            return det;
            
        }

    }
    
    // Mencari matriks kofaktor dari sebuah matriks 
    public Matrix kofaktor(Matrix mIn, int row, int col){
        // Menerima input berupa matriks serta baris dan kolom yang tidak ingin dimasukkan 

        // Inisialisasi matrix
        Matrix mTemp = new Matrix(mIn.getRowLength()-1, mIn.getColLength()-1);
        int a = 0; 
        int b = 0;
        for(int i =0;i < mIn.getRowLength();i++){
            for(int j =0; j < mIn.getColLength(); j++){
                if(i != row && j != col){
                    double temp = mIn.getElmt(i,j);
                    mTemp.setElmt(a,b,temp);
                    b+=1;
                    if(b == mIn.getColLength()-1){
                        b = 0; 
                        a++;
                    }
                }
            }
        }
        return mTemp;
    }

    // Membentuk matriks adjoin dari sebuah matriks
    public Matrix adj(Matrix mInput){
        Matrix mTemp = new Matrix(mInput.getRowLength(), mInput.getColLength());
        for(int i =0 ; i < mInput.getRowLength(); i++){
            for(int j=0; j <mInput.getColLength(); j++){
                double temp = Math.pow(-1, i+j) * detKof(kofaktor(mInput,i,j));
                mTemp.setElmt(i,j,temp);
            }
        }
        return mTemp.transpose();
    }
    // Mencari nilai matriks inverse dari sebuah matriks
    public Matrix inverse(Matrix m){
        double determinan = detKof(m);
        Matrix adj = adj(m);
        System.out.println(determinan);
        adj.multiplyByConst(adj, (1/determinan));
        return adj;
    }
    // Membuat matriks baru dengan membuang baris terakhir dalam matriks
    public Matrix delLastRow (Matrix m){
        Matrix mTemp = new Matrix(m.getRowLength() - 1, m.getColLength());
        for (int i =0 ; i < m.getRowLength()-1; i++){
            for (int j = 0; j < mTemp.getColLength(); j++ ){
                mTemp.setElmt(i, j, m.getElmt(i, j));
            }
        }
        return mTemp;
    }

    

    public void cramer(Matrix mIn){
        
        // Inisiasi matriks original
        Matrix mOriginal = new Matrix(mIn.getRowLength(), mIn.getColLength()-1);
        for (int i = 0; i < mIn.getRowLength(); i++){
            for (int j = 0; j < mIn.getColLength()-1; j++){
                mOriginal.setElmt(i, j, mIn.getElmt(i, j));
            }
        }

        double det = detKof(mOriginal);
        if (det == 0){
            System.out.println("Matriks tidak memiliki solusi.");
        }
        else{
            for (int i = 0; i <= mOriginal.getColIdx(); i++){
                Matrix mTemp = copyMatrix(mIn);
                Matrix mNew = new Matrix(mTemp.getColLength(), mTemp.getRowLength());
                Matrix mTranspose = mTemp.transpose();
                mTranspose.swapRow(i, mTranspose.getRowIdx());
                mNew = delLastRow(mTranspose);
                mNew = mNew.transpose();

                System.out.println("Bentuk Matriks: ");
                mNew.printMatrix();
                System.out.println("===============");
                mOriginal.printMatrix();
                System.out.println("Solusi : ");
                System.out.println("X" + (i+1) + " = " + detKof(mNew) / det);
            }
        }
    }

}
