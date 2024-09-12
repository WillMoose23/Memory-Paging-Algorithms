# Memory-Paging-Algorithms
A project for my Operating Systems course in College that demonstrates paging algorithms


Description:
This project demonstrates three various algorithms for Memory Paging in an Operating System. 
Paging is a technique used by the operating system to optimize computer memory usage. 
It also allows the user to utililze drive memory as a replacement for RAM when resources are are unavailable albiet at slower speeds. 

Three algorithms are explored in this program, First In First Out, Least Recently Used, and Optimal Page Replacement.

First In First Out (FIFO) - The OS creates a queue and the oldest page in the front of the queue is replaced when a page fault occurs.

Optimal Page Replacement - The optimal paging algorithm replaces the page that will not be used for the longest period in the future. It is theoretically the most efficient but impractical in real systems because it requires knowing future memory references.

Least Recently Used (LRU) - The OS will replace the page with the one that was used the least recently. To highlight the difference between FIFO and LRU, FIFO will replace the oldest page regardless of when it was last accessed whereas LRU will only replace the page that hasn't been used in the longest time.
