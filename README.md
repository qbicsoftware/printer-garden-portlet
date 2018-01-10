# Printer-Garden

This software can be used to add either new printers or printer-project associations and their attributes to the QBiC database. 
It can be accessed via qPortal.
 
## Getting Started 

In the initial view select which table you wish to edit. 

### Printer

The printer view allows to add a new printer entry, modify existing ones, or delete one. 

If a new printer should be added, the required fields need to be added:

```
Name
Location
URL
Status
Type
```

It is necessary, that the tuple of (Name, Location) is unique. Additionally, a correctly formatted URL needs be provided.

If an existing entry needs to be modified, simply click on the respective field, change the entry, and press save.

The deletion of an entry is accomplished via the respective entry ID. Either type in the ID into the field or select 
it from the dropdown list and press 'Delete'.


### Printer-Project Association

The printer-project association table allows to combine an existing printer with an existing project. The respective printers
and project can be selected from a dropdown menu. The selections are searchable by typing in substrings of their values.
A status of the association needs to be provided.

The deletion of an entry is accomplished via the respective entry ID. Either type in the ID into the field or select 
it from the dropdown list and press 'Delete'.

