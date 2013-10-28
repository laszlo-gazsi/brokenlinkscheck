var ApplicationContext = [];

// name space for the SOAP requests
ApplicationContext.namespace = 'http://ws.brokenlinkscheck.lacyg.info/';

// pagination information
ApplicationContext.currentUseCase = 'all_tasks';
ApplicationContext.currentPage = 0;
ApplicationContext.pageSize = 10;

ApplicationContext.currentTask = null;
ApplicationContext.fastMode = null;
