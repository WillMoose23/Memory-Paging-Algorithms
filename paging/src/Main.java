//William Moosakhanian
//CSC 139 HW #4
//Memory Management & Paging

//Note* All test case files and output file (myfile), are located in the same folder as source code!

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;


public class Main
{
    public static void main(String [] args)
    {
       //String array[] = new String[5];
        String inputPath = "src\\input10.txt";
        String outputPath = "src\\myfile.txt";

        int index = 0;

//        for(int i = 0; i<array.length; i++)
//        {
//            index = i;
//            array[i] = "src\\test" + (index+1) + ".txt";
//            readData(array[i]);
//        }

        readData(inputPath);
        //clearFile(outputPath);
    }

    public static void readData(String fileName)
    {
        int page = 0;
        int frame = 0;
        int pageReq = 0;

        int pt[];

        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            Scanner in = new Scanner(br);

            page = Integer.parseInt(in.next());
            frame = Integer.parseInt(in.next());
            pageReq = Integer.parseInt(in.next());

            pt = new int[pageReq];

            in.nextLine();

            for(int i = 0; i < pageReq; i++)
            {
                pt[i] = Integer.parseInt(in.next());
                in.nextLine();
            }

            FIFO(page, frame, pageReq, pt);
            Optimal(page, frame, pageReq, pt);
            LRU(page, frame, pageReq, pt);

            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void clearFile(String path)
    {
        try (FileWriter writer = new FileWriter(path)) {}
        catch (IOException e) {e.printStackTrace();}
    }

    public static void FIFO(int pageNum, int frameNum, int req, int[]arr)
    {
        Queue<Integer> queue = new LinkedList<>();
        int table[] = new int[frameNum];
        boolean check = false;
        int temp = 0;
        int pageFaults = 0;
        int next = 0;

        int i = 0;
        int j = 0;
        int k = 0;

        String output = "";

        output = output + "\nFIFO\n";
        while(i< arr.length)
        {
            temp = arr[i];
            check = false;

            j = 0;
            while(j< table.length)
            {
                if(temp == table[j])
                {
                    output += "Page " + arr[i] + " already in frame " + j + "\n";
                    check = true;
                    break;
                }
                j++;
            }

            if (!check)
            {
                pageFaults++;
                if(next < table.length)
                {
                    output +="Page " + arr[i] + " loaded into frame " + next + "\n";
                    table[next] = temp;
                    next++;
                }
                else
                {
                    int remove = queue.poll();
                    k = 0;
                    while(k<table.length)
                    {
                        if(table[k] == remove)
                        {
                            output += "Page " + table[k] + " unloaded from frame " + k + ", " +
                                    "Page " + arr[i] + " loaded into frame " + k + "\n";
                            table[k] = temp;
                            break;
                        }
                        k++;
                    }
                }
                queue.offer(temp);
            }
            i++;
        }
        output += pageFaults + " page faults" + "\n";
        //System.out.print(output);

        try(FileWriter writer = new FileWriter("src\\myfile.txt", true))
        {
            writer.write(output);
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void Optimal(int pageNum, int frameNum, int req, int[]arr)
    {
        ArrayList <Page> table = new ArrayList<>();

        boolean check = false;
        boolean found = false;
        boolean infiniteFlag = false;

        int pageFaults = 0;

        String output = "";

        output += "\nOptimal\n";

        for(int i = 0; i < arr.length; i++)
        {
            Page temp = new Page(arr[i],-1);

            for(int j = 0; j < table.size(); j++)
            {
                if(temp.getIndex() == table.get(j).getIndex())
                {
                    output += "Page: " + arr[i] + " already in frame " + j + "\n";
                    check = true;
                    temp.setTime(table.get(j).getTime());
                    break;
                }
            }

            double x = -1;
            for(int j = i +1; j<arr.length; j++)
            {
                if (temp.getIndex() == arr[j])
                {
                    x = j;
                    found = true;
                    break;
                }
            }

            if(!found)
            {
                x = Double.POSITIVE_INFINITY;
            }

            temp.setTime(x);

            if(!check)
            {
                pageFaults++;
                if(table.size() < frameNum)
                {
                    table.add(temp);
                    output += "Page: " + arr[i] + " loaded into frame " + table.indexOf(temp) + "\n";
                }
                else
                {
                    double max = Double.MIN_VALUE;
                    double og_idx = 0;
                    double idx = -1;
                    double first = 0;

                    for(int p = 0; p< table.size(); p++)
                    {
                        if(table.get(p).getIndex() == Double.POSITIVE_INFINITY)
                        {
                            first++;
                            if(first == 1)
                            {
                                og_idx = p;
                            }
                            else
                            {
                                infiniteFlag = true;
                                break;
                            }
                        }
                    }

                    if(infiniteFlag)
                    {
                        table.set((int)og_idx, temp);
                    }
                    else
                    {
                        for(int j = 0; j < table.size(); j++)
                        {
                            if(table.get(j).getTime() > max)
                            {
                                max = table.get(j).getTime();
                                idx = j;
                            }
                        }
                        double unload = table.get((int)idx).getIndex();
                        table.set((int)idx, temp);
                        output += "Page: " + (int)unload + " unloaded from frame " + table.indexOf(temp) + ", " +
                                "Page: " + arr[i]  + " loaded into frame " + table.indexOf(temp) + "\n";
                    }
                }
            }
            else
            {
                for(int k = 0; k < table.size(); k++)
                {
                    if(table.get(k).getIndex() == temp.getIndex())
                    {
                        table.get(k).setTime(x);
                    }
                }
            }
            check = false;
            found = false;
            infiniteFlag = false;
        }
        output += pageFaults + " page faults\n";

        try(FileWriter writer = new FileWriter("src\\myfile.txt", true))
        {
            writer.write(output);
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void LRU(int pageNum, int frameNum, int req, int[]arr)
    {
        Page arrTable[] = new Page[arr.length];
        ArrayList <Page> table = new ArrayList<>();

        boolean check = false;
        double temp = 0;
        int pageFaults = 0;
        String output = "";

        for (int k = 0; k < arr.length; k++)
        {
            arrTable[k] = new Page(arr[k],k+1);
        }

        output += "\nLRU\n";
        for(int i = 0; i < arrTable.length; i++)
        {
            temp = arrTable[i].getIndex();
            for(int j = 0; j < table.size(); j++)
            {
                if(temp == table.get(j).getIndex())
                {
                    output += "Page: " + arr[i] + " already in frame " + j + "\n";
                    check = true;
                    break;
                }
            }

            if(!check)
            {
                pageFaults++;
                if(table.size() < frameNum)
                {
                    table.add(arrTable[i]);
                    output += "Page: " + arr[i] + " loaded into frame " + table.indexOf(arrTable[i]) + "\n";
                }
                else
                {
                    double min = Double.MAX_VALUE;
                    double idx = -1;

                    for(int j = 0; j < table.size(); j++)
                    {
                        if(table.get(j).getTime() < min)
                        {
                            min = table.get(j).getTime();
                            idx = j;
                        }

                    }
                    double unload = table.get((int)idx).getIndex();
                    table.set((int)idx, arrTable[i]);
                    output += "Page: " + (int)unload + " unloaded from frame " + (int)idx + ", " +
                            "Page: " + arr[i]  + " loaded into frame " + (int)idx + "\n";
                }
            }
            else
            {
                for(int k = 0; k < table.size(); k++)
                {
                    if(table.get(k).getIndex() == temp)
                    {
                        table.get(k).setTime(i+1);
                    }
                }

            }
            check = false;
        }
        output += pageFaults + " page faults\n";

        try(FileWriter writer = new FileWriter("src\\myfile.txt", true))
        {
            writer.write(output);
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void printTable(ArrayList <Page> x)
    {
        for(int i = 0; i < x.size(); i++)
        {
            System.out.print(x.get(i));
        }
        System.out.print("\n\n");
    }

    public static void print(String s)
    {
        System.out.println(s);
    }

    public static void ln()
    {
        System.out.println();
    }
}


class Page
{
    private double index = 0;
    private double time = 0;

    Page(double a, double b)
    {
        index = a;
        time = b;
    }

    Page(double a, int b)
    {
        index = a;
        time = b;
    }

    public double getIndex()
    {
        return this.index;
    }

    public void setIndex(double i)
    {
        index = i;
    }

    public double getTime()
    {
        return this.time;
    }

    public void setTime(double t)
    {
        time = t;
    }

    public String toString()
    {
        return (int)this.getIndex() + "\t" + (int)this.getTime(); // 4 2
    }
}


