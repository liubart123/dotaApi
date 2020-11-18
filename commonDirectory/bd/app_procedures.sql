--users
CREATE OR REPLACE PROCEDURE insert_user(
    nameP          VARCHAR,
    hash_passwordP VARCHAR,
    user_roleP     VARCHAR,
    resultId OUT INTEGER)
IS
  exist PLS_INTEGER;
BEGIN
  SELECT COUNT(1) INTO exist FROM app_users WHERE name = nameP AND ROWNUM = 1;
  IF exist    = 1 THEN
    resultId := -1;
  ELSE
    INSERT
    INTO app_users
      (
        name,
        hash_password,
        user_role
      )
      VALUES
      (
        nameP,
        hash_passwordP,
        user_roleP
      );
    SELECT id INTO resultId FROM app_users WHERE name = nameP AND ROWNUM = 1;
  END IF;
END;
/
CREATE OR REPLACE PROCEDURE get_user(
    curs OUT SYS_REFCURSOR,
    login VARCHAR)
IS
BEGIN
  OPEN curs FOR SELECT * FROM app_users WHERE name=login AND rownum = 1;
END;
/
CREATE OR REPLACE PROCEDURE exist_user(
    nameP VARCHAR,
    resultId OUT BOOLEAN)
IS
  exist PLS_INTEGER;
BEGIN
  SELECT COUNT(1) INTO exist FROM app_users WHERE name = nameP AND ROWNUM = 1;
  IF exist   = 1 THEN
    resultId:=true;
  ELSE
    resultId := false;
  END IF;
END;
/
SELECT * FROM app_users;
--selections
CREATE OR REPLACE PROCEDURE insert_selection(
    nameP                 VARCHAR,
    durationMinP          INT,
    durationMaxP          INT,
    patchMinP             INT,
    patchMaxP             INT,
    dateMinP              DATE,
    dateMaxP              DATE,
    investigated_hero_idP INT,
    user_idP              INT,
    resultId OUT INT)
IS
  exist PLS_INTEGER;
BEGIN
  SELECT COUNT(1)
  INTO exist
  FROM selections
  WHERE name  = nameP
  AND user_id = user_idP
  AND ROWNUM  = 1;
  IF exist    = 1 THEN
    resultId :=-1;
    --      SELECT id
    --      INTO resultId
    --      FROM selections
    --        WHERE name = nameP and user_id = user_idP
    --       AND ROWNUM = 1;
  ELSE
    INSERT
    INTO selections
      (
        name,
        durationMin,
        durationMax,
        patchMin,
        patchMax,
        dateMin,
        dateMax,
        investigated_hero_id,
        user_id
      )
      VALUES
      (
        nameP,
        durationMinP,
        durationMaxP,
        patchMinP,
        patchMaxP,
        dateMinP,
        dateMaxP,
        investigated_hero_idP,
        user_idP
      );
    SELECT id INTO resultId FROM selections WHERE name=nameP AND user_id=user_idP;
  END IF;
END;
/
CREATE OR REPLACE PROCEDURE update_selection(
    nameP                 VARCHAR,
    durationMinP          INT,
    durationMaxP          INT,
    patchMinP             INT,
    patchMaxP             INT,
    dateMinP              DATE,
    dateMaxP              DATE,
    investigated_hero_idP INT,
    user_idP              INT,
    idP                   INT)
IS
BEGIN
  UPDATE selections
  SET name              = nameP,
    durationMin         =durationMinP,
    durationMax         =durationMaxP,
    patchMin            =patchMinP,
    patchMax            =patchMaxP,
    dateMin             =dateMinP,
    dateMax             =dateMaxP,
    investigated_hero_id=investigated_hero_idP,
    user_id             =user_idP
  WHERE id              =idp;
END;
/
SELECT * FROM selections;
SELECT * FROM selections_heroes;
SELECT * FROM selections_items;
/
CREATE OR REPLACE PROCEDURE insert_selection_heroes(
    selection_idp INTEGER,
    hero_idP      INTEGER,
    hero_roleP    VARCHAR )
IS
  exist PLS_INTEGER;
BEGIN
  SELECT COUNT(1)
  INTO exist
  FROM selections_heroes
  WHERE selection_id = selection_idp
  AND hero_id        = hero_idP
  AND hero_role      = hero_roleP
  AND ROWNUM         = 1;
  IF exist          <> 1 THEN
    INSERT
    INTO selections_heroes
      (
        selection_id,
        hero_id,
        hero_role
      )
      VALUES
      (
        selection_idP,
        hero_idP,
        hero_roleP
      );
  END IF;
END;
/
CREATE OR REPLACE PROCEDURE clear_selection_heroes
  (
    selection_idp INTEGER
  )
IS
BEGIN
  DELETE FROM selections_heroes WHERE selection_id = selection_idp;
END;
/
CREATE OR REPLACE PROCEDURE insert_selection_items(
    selection_idp INTEGER,
    item_idP      INTEGER,
    item_roleP    VARCHAR )
IS
  exist PLS_INTEGER;
BEGIN
  SELECT COUNT(1)
  INTO exist
  FROM selections_items
  WHERE selection_id = selection_idp
  AND item_id        = item_idP
  AND item_role      = item_roleP
  AND ROWNUM         = 1;
  IF exist          <> 1 THEN
    INSERT
    INTO selections_items
      (
        selection_id,
        item_id,
        item_role
      )
      VALUES
      (
        selection_idP,
        item_idP,
        item_roleP
      );
  END IF;
END;
/
CREATE OR REPLACE PROCEDURE clear_selection_items
  (
    selection_idp INTEGER
  )
IS
BEGIN
  DELETE FROM selections_items WHERE selection_id = selection_idp;
END;
/
CREATE OR REPLACE PROCEDURE update_selection(
    nameP                 VARCHAR,
    durationMinP          INTEGER,
    durationMaxP          INTEGER,
    patchMinP             INTEGER,
    patchMaxP             INTEGER,
    dateMinP              DATE,
    dateMaxP              DATE,
    investigated_hero_idP INTEGER,
    user_idP              INTEGER,
    idP                   INTEGER,
    resultId OUT INTEGER)
IS
  exist PLS_INTEGER;
BEGIN
  SELECT COUNT(1) INTO exist FROM selections WHERE id = idP AND ROWNUM = 1;
  IF exist = 1 THEN
    UPDATE selections
    SET name              =nameP,
      durationMin         =durationMinP,
      durationMax         =durationMaxP,
      patchMin            =patchMinP,
      patchMax            =patchMaxP,
      dateMin             =dateMinP,
      dateMax             =dateMaxP,
      investigated_hero_id=investigated_hero_idP,
      user_id             =user_idP
    WHERE id              =idp;
    SELECT id INTO resultId FROM selections WHERE id=idp;
  ELSE
    resultId:=-1;
  END IF;
END;
/
CREATE OR REPLACE PROCEDURE delete_selection(
    idP INTEGER)
IS
BEGIN
  DELETE FROM SELECTIONS_HEROES WHERE SELECTION_ID=idp;
  DELETE FROM SELECTIONS_ITEMS WHERE SELECTION_ID=idp;
  DELETE FROM selections_bubblechart WHERE SELECTION_ID=idp;
  DELETE FROM selections_barchart WHERE SELECTION_ID=idp;
  DELETE FROM selections WHERE id=idp;
END;
/
BEGIN
  delete_selection(21);
END;
/
SELECT * FROM app_users;
/
CREATE OR REPLACE PROCEDURE SELECT_selections(
    curs OUT SYS_REFCURSOR,
    user_idp INTEGER)
IS
BEGIN
  OPEN curs FOR SELECT * FROM selections WHERE user_id=user_Idp order by name;
END;
/
CREATE OR REPLACE PROCEDURE get_selection(
    curs OUT SYS_REFCURSOR,
    idp INTEGER)
IS
BEGIN
  OPEN curs FOR SELECT * FROM selections WHERE id=Idp;
END;
/
CREATE OR REPLACE PROCEDURE SELECT_selections_items(
    curs OUT SYS_REFCURSOR,
    idp INTEGER)
IS
BEGIN
  OPEN curs FOR SELECT * FROM selections_items WHERE selection_id=idp;
END;
/
CREATE OR REPLACE PROCEDURE SELECT_selections_heroes(
    curs OUT SYS_REFCURSOR,
    idp INTEGER)
IS
BEGIN
  OPEN curs FOR SELECT * FROM selections_heroes WHERE selection_id=idp;
END;
/
--BubbleChart
CREATE OR REPLACE PROCEDURE insert_bubblechart(
    minCountOfMatchesP INTEGER,
    nameP              VARCHAR,
    xAxisP             VARCHAR,
    yAxisP             VARCHAR,
    xScaleP FLOAT,
    user_idP INTEGER,
    resultId OUT INTEGER)
IS
  exist PLS_INTEGER;
BEGIN
  SELECT COUNT(1)
  INTO exist
  FROM BubbleChart
  WHERE name  = nameP
  AND user_id = user_idP
  AND ROWNUM  = 1;
  IF exist    = 1 THEN
    resultId :=-1;
  ELSE
    INSERT
    INTO BubbleChart
      (
        minCountOfMatches,
        name,
        xAxis,
        yAxis,
        xScale,
        user_id
      )
      VALUES
      (
        minCountOfMatchesP,
        nameP,
        xAxisP,
        yAxisP,
        xScaleP,
        user_idP
      );
    SELECT id
    INTO resultId
    FROM BubbleChart
    WHERE name =nameP
    AND user_id=user_idP;
  END IF;
END;
/
CREATE OR REPLACE PROCEDURE insert_selections_bubblechart(
    selection_idP   INTEGER,
    bubblechart_idP INTEGER )
IS
BEGIN
  INSERT
  INTO selections_bubblechart
    (
      selection_id,
      bubblechart_id
    )
    VALUES
    (
      selection_idP,
      bubblechart_idp
    );
END;
/
CREATE OR REPLACE PROCEDURE clear_selections_bubblechart
  (
    bubblechart_idP INTEGER
  )
IS
BEGIN
  DELETE FROM selections_bubblechart WHERE bubblechart_id = bubblechart_idP;
END;
/
CREATE OR REPLACE PROCEDURE update_bubblechart(
    minCountOfMatchesP INTEGER,
    nameP              VARCHAR,
    xAxisP             VARCHAR,
    yAxisP             VARCHAR,
    xScaleP FLOAT,
    idp INTEGER)
IS
  exist PLS_INTEGER;
BEGIN
  UPDATE BubbleChart
  SET minCountOfMatches = minCountOfMatchesp,
    name                =nameP,
    xAxis               =xAxisP,
    yAxis               =yAxisP,
    xScale              =xScaleP
  WHERE id              =idp;
END;
/
CREATE OR REPLACE PROCEDURE delete_bubblechart(
    idP INTEGER)
IS
BEGIN
  DELETE FROM selections_bubblechart WHERE BUBBLECHART_ID=idp;
  DELETE FROM bubblechart WHERE id=idp;
END;
/
CREATE OR REPLACE PROCEDURE SELECT_bubblecharts(
    curs OUT SYS_REFCURSOR,
    user_idp INTEGER)
IS
BEGIN
  OPEN curs FOR SELECT * FROM bubblechart WHERE user_id=user_Idp order by name;
END;
/
CREATE OR REPLACE PROCEDURE SELECT_bubblechart(
    curs OUT SYS_REFCURSOR,
    idp INTEGER)
IS
BEGIN
  OPEN curs FOR SELECT * FROM bubblechart WHERE id=Idp;
END;
/
CREATE OR REPLACE PROCEDURE SELECT_bubblecharts_selections(
    curs OUT SYS_REFCURSOR,
    idp INTEGER)
IS
BEGIN
  OPEN curs FOR SELECT * FROM selections_bubblechart WHERE bubblechart_id=Idp;
END;
/
SELECT * FROM selections_bubblechart;
--BarChart
SELECT * FROM barchart;
SELECT * FROM selections_barchart;
SELECT * FROM BarChart_labels;
/
CREATE OR REPLACE PROCEDURE insert_barchart(
    minCountOfMatchesP INTEGER,
    nameP              VARCHAR,
    xAxisP             VARCHAR,
    yAxisP             VARCHAR,
    user_idP           INTEGER,
    resultId OUT INTEGER)
IS
  exist PLS_INTEGER;
BEGIN
  SELECT COUNT(1)
  INTO exist
  FROM barChart
  WHERE name  = nameP
  AND user_id = user_idP
  AND ROWNUM  = 1;
  IF exist    = 1 THEN
    resultId :=-1;
  ELSE
    INSERT
    INTO barChart
      (
        minCountOfMatches,
        name,
        xAxis,
        yAxis,
        user_id
      )
      VALUES
      (
        minCountOfMatchesP,
        nameP,
        xAxisP,
        yAxisP,
        user_idP
      );
    SELECT id INTO resultId FROM barChart WHERE name=nameP AND user_id=user_idP;
  END IF;
END;
/
CREATE OR REPLACE PROCEDURE insert_selections_barchart(
    selection_idP INTEGER,
    barchart_idP  INTEGER )
IS
BEGIN
  INSERT
  INTO selections_barchart
    (
      selection_id,
      barchart_id
    )
    VALUES
    (
      selection_idP,
      barchart_idp
    );
END;
/
CREATE OR REPLACE PROCEDURE clear_selections_barchart
  (
    barchart_idP INTEGER
  )
IS
BEGIN
  DELETE FROM selections_barchart WHERE barchart_id = barchart_idP;
END;
/
CREATE OR REPLACE PROCEDURE update_barchart(
    minCountOfMatchesP INTEGER,
    nameP              VARCHAR,
    xAxisP             VARCHAR,
    yAxisP             VARCHAR,
    idp                INTEGER)
IS
  exist PLS_INTEGER;
BEGIN
  UPDATE barChart
  SET minCountOfMatches = minCountOfMatchesp,
    name                =nameP,
    xAxis               =xAxisP,
    yAxis               =yAxisP
  WHERE id              =idp;
END;
/
CREATE OR REPLACE PROCEDURE delete_barchart(
    idP INTEGER)
IS
BEGIN
  DELETE FROM selections_barchart WHERE barchart_id=idp;
  DELETE FROM BarChart_labels WHERE barchart_id=idp;
  DELETE FROM barchart WHERE id=idp;
END;
/
CREATE OR REPLACE PROCEDURE SELECT_barcharts(
    curs OUT SYS_REFCURSOR,
    user_idp INTEGER)
IS
BEGIN
  OPEN curs FOR SELECT * FROM barchart WHERE user_id=user_Idp order by name;
END;
/
CREATE OR REPLACE PROCEDURE SELECT_barchart(
    curs OUT SYS_REFCURSOR,
    idp INTEGER)
IS
BEGIN
  OPEN curs FOR SELECT * FROM barchart WHERE id=Idp;
END;
/
CREATE OR REPLACE PROCEDURE SELECT_barcharts_selections(
    curs OUT SYS_REFCURSOR,
    idp INTEGER)
IS
BEGIN
  OPEN curs FOR SELECT * FROM selections_barchart WHERE barchart_id=idp;
END;
/
CREATE OR REPLACE PROCEDURE SELECT_barcharts_labels(
    curs OUT SYS_REFCURSOR,
    idp INTEGER)
IS
BEGIN
  OPEN curs FOR SELECT * FROM BarChart_labels WHERE barchart_id=idp;
END;
/
CREATE OR REPLACE PROCEDURE insert_barchart_labels(
    BarChart_idp INTEGER,
    labelp       VARCHAR )
IS
BEGIN
  INSERT
  INTO BarChart_labels
    (
      BarChart_id,
      label
    )
    VALUES
    (
      BarChart_idp,
      labelp
    );
END;
/
CREATE OR REPLACE PROCEDURE clear_barchart_labels
  (
    barchart_idP INTEGER
  )
IS
BEGIN
  DELETE FROM BarChart_labels WHERE barchart_id = barchart_idP;
END;
/
--LineChart
CREATE OR REPLACE PROCEDURE insert_linechart(
    minCountOfMatchesP INTEGER,
    nameP              VARCHAR,
    xAxisP             VARCHAR,
    yAxisP             VARCHAR,
    user_idP           INTEGER,
    countOfLabelsp     INTEGER,
    isDescp            NUMBER,
    selectionIdp       INTEGER,
    resultId OUT INTEGER)
IS
  exist PLS_INTEGER;
BEGIN
  SELECT COUNT(1)
  INTO exist
  FROM linechart
  WHERE name  = nameP
  AND user_id = user_idP
  AND ROWNUM  = 1;
  IF exist    = 1 THEN
    resultId :=-1;
  ELSE
    INSERT
    INTO linechart
      (
        minCountOfMatches,
        name,
        xAxis,
        yAxis,
        countOfLabels,
        isDesc,
        selectionId,
        user_id
      )
      VALUES
      (
        minCountOfMatchesP,
        nameP,
        xAxisP,
        yAxisP,
        countOfLabelsp,
        isDescp,
        selectionIdp,
        user_idP
      );
    SELECT id INTO resultId FROM linechart WHERE name=nameP AND user_id=user_idP;
  END IF;
END;
/
CREATE OR REPLACE PROCEDURE update_linechart(
    minCountOfMatchesP INTEGER,
    nameP              VARCHAR,
    xAxisP             VARCHAR,
    yAxisP             VARCHAR,
    countOfLabelsp     INTEGER,
    isDescp            NUMBER,
    selectionIdp       INTEGER,
    idp                INTEGER)
IS
  exist PLS_INTEGER;
BEGIN
  UPDATE linechart
  SET minCountOfMatches = minCountOfMatchesp,
    name                =nameP,
    xAxis               =xAxisP,
    yAxis               =yAxisP,
    countOfLabels       =countOfLabelsp,
    isDesc              =isDescp,
    selectionId         =selectionIdp
  WHERE id              =idp;
END;
/
CREATE OR REPLACE PROCEDURE delete_linechart(
    idP INTEGER)
IS
BEGIN
  DELETE FROM linechart WHERE id=idp;
END;
/
CREATE OR REPLACE PROCEDURE SELECT_linecharts(
    curs OUT SYS_REFCURSOR,
    user_idp INTEGER)
IS
BEGIN
  OPEN curs FOR SELECT * FROM linechart WHERE user_id=user_Idp order by name;
END;
/
CREATE OR REPLACE PROCEDURE SELECT_linechart(
    curs OUT SYS_REFCURSOR,
    idp INTEGER)
IS
BEGIN
  OPEN curs FOR SELECT * FROM linechart WHERE id=idp;
END;
/
SELECT * FROM linechart;
--xml
--usesrs
/
CREATE OR REPLACE DIRECTORY EXPORTFILE
AS
  'C:/xmlFiles';
CREATE OR REPLACE PROCEDURE export_users
IS
  file1 utl_file.file_type;
  xrow CLOB;
BEGIN
  file1 := UTL_FILE.FOPEN('EXPORTFILE','users.txt','w');
  SELECT XMLELEMENT(root,XMLAGG(XMLELEMENT(appuser, XMLATTRIBUTES(e.id, e.name, e.user_role,e.hash_password)))).getCLOBVal() AS xmlsads
  INTO xrow
  FROM app_users e;
  utl_file.put(file1,xrow);
  utl_file.fclose(file1);
END;
/
CREATE OR REPLACE PROCEDURE import_users
IS
  file1 utl_file.file_type;
  xrow CLOB;
BEGIN
  file1 := UTL_FILE.FOPEN('EXPORTFILE','users.txt','r');
  utl_file.get_line(file1,xrow);
  merge INTO app_users cur_t USING
  (SELECT extractValue(value(t),'//@ID') id,
    extractValue(value(t),'//@NAME') name,
    extractValue(value(t),'//@HASH_PASSWORD') HASH_PASSWORD,
    extractValue(value(t),'//@USER_ROLE') USER_ROLE
  FROM TABLE(XMLSequence(XMLType(xrow).extract('//APPUSER'))) t
  ) imp_t ON (cur_t.id=imp_t.id)
WHEN NOT matched THEN
  INSERT
    (
      cur_t.id,
      cur_t.name,
      cur_t.HASH_PASSWORD,
      cur_t.USER_ROLE
    )
    VALUES
    (
      imp_t.id,
      imp_t.name,
      imp_t.HASH_PASSWORD,
      imp_t.USER_ROLE
    );
  utl_file.fclose(file1);
END;
/
--selections
SELECT * FROM selections;
CREATE OR REPLACE PROCEDURE export_selections
IS
  file1 utl_file.file_type;
  xrow CLOB;
BEGIN
  SELECT XMLELEMENT(root,XMLAGG(XMLELEMENT(el, XMLATTRIBUTES(e.id, e.name, e.durationmin,e.durationmax,e.patchmin,e.patchmax,e.datemin,e.datemax,e.investigated_hero_id,e.user_id) ))).getCLOBVal() AS xm
  INTO xrow
  FROM selections e;
  DBMS_XSLPROCESSOR.CLOB2FILE( xrow, 'EXPORTFILE', 'selections.TXT');
END;
/
CREATE OR REPLACE PROCEDURE import_selections
IS
  xrow CLOB;
BEGIN
  xrow := DBMS_XSLPROCESSOR.READ2CLOB( 'EXPORTFILE', 'selections.TXT',0);
  merge INTO selections cur_t USING
  (SELECT extractValue(value(t),'//@ID') ID ,
    extractValue(value(t),'//@NAME') NAME ,
    extractValue(value(t),'//@DURATIONMIN') DURATIONMIN ,
    extractValue(value(t),'//@DURATIONMAX') DURATIONMAX ,
    extractValue(value(t),'//@PATCHMIN') PATCHMIN ,
    extractValue(value(t),'//@PATCHMAX') PATCHMAX ,
    TO_DATE(extractValue(value(t),'//@DATEMIN'),'YYYY-MM-DD') DATEMIN ,
    TO_DATE(extractValue(value(t),'//@DATEMAX'),'YYYY-MM-DD') DATEMAX ,
    extractValue(value(t),'//@INVESTIGATED_HERO_ID') INVESTIGATED_HERO_ID ,
    extractValue(value(t),'//@USER_ID') USER_ID
  FROM TABLE(XMLSequence(XMLType(xrow) .extract('//EL'))) t
  ) imp_t ON (cur_t.id=imp_t.id)
WHEN NOT matched THEN
  INSERT
    (
      cur_t.id,
      cur_t. NAME ,
      cur_t. DURATIONMIN ,
      cur_t. DURATIONMAX ,
      cur_t. PATCHMIN ,
      cur_t. PATCHMAX ,
      cur_t. DATEMIN ,
      cur_t. DATEMAX ,
      cur_t. INVESTIGATED_HERO_ID ,
      cur_t. USER_ID
    )
    VALUES
    (
      imp_t.id,
      imp_t. NAME ,
      imp_t. DURATIONMIN ,
      imp_t. DURATIONMAX ,
      imp_t. PATCHMIN ,
      imp_t. PATCHMAX ,
      imp_t. DATEMIN ,
      imp_t. DATEMAX ,
      imp_t. INVESTIGATED_HERO_ID ,
      imp_t. USER_ID
    );
END;
/
--selections_heroes
SELECT * FROM selections_heroes;
CREATE OR REPLACE PROCEDURE export_selections_heroes
IS
  file1 utl_file.file_type;
  xrow CLOB;
BEGIN
  SELECT XMLELEMENT(root,XMLAGG(XMLELEMENT(el, XMLATTRIBUTES( e.selection_id, e.hero_id, e.hero_role ) ))).getCLOBVal() AS xm
  INTO xrow
  FROM selections_heroes e;
  DBMS_XSLPROCESSOR.CLOB2FILE( xrow, 'EXPORTFILE', 'selections_heroes.TXT');
END;
/
CREATE OR REPLACE PROCEDURE import_selections_heroes
IS
  xrow CLOB;
BEGIN
  xrow := DBMS_XSLPROCESSOR.READ2CLOB( 'EXPORTFILE', 'selections_heroes.TXT',0);
  merge INTO selections_heroes cur_t USING
  (SELECT extractValue(value(t),'//@ SELECTION_ID ') SELECTION_ID,
    extractValue(value(t),'//@ HERO_ID ') HERO_ID,
    extractValue(value(t),'//@ HERO_ROLE ') HERO_ROLE
  FROM TABLE(XMLSequence(XMLType(xrow) .extract('//EL'))) t
  ) imp_t ON (cur_t.SELECTION_ID=imp_t.SELECTION_ID AND cur_t.HERO_ID=imp_t.HERO_ID AND cur_t.HERO_ROLE=imp_t.HERO_ROLE)
WHEN NOT matched THEN
  INSERT
    (
      cur_t. SELECTION_ID ,
      cur_t. HERO_ID ,
      cur_t. HERO_ROLE
    )
    VALUES
    (
      imp_t. SELECTION_ID ,
      imp_t. HERO_ID ,
      imp_t. HERO_ROLE
    );
END;
/
BEGIN
  export_selections_heroes();
  import_selections_heroes();
END;
/
--selections_items
SELECT * FROM selections_items;
CREATE OR REPLACE PROCEDURE export_selections_items
IS
  file1 utl_file.file_type;
  xrow CLOB;
BEGIN
  SELECT XMLELEMENT(root,XMLAGG(XMLELEMENT(el, XMLATTRIBUTES( e.selection_id, e.ITEM_ID, e.ITEM_ROLE ) ))).getCLOBVal() AS xm
  INTO xrow
  FROM selections_items e;
  DBMS_XSLPROCESSOR.CLOB2FILE( xrow, 'EXPORTFILE', 'selections_items.TXT');
END;
/
CREATE OR REPLACE PROCEDURE import_selections_items
IS
  xrow CLOB;
BEGIN
  xrow := DBMS_XSLPROCESSOR.READ2CLOB( 'EXPORTFILE', 'selections_items.TXT',0);
  merge INTO selections_items cur_t USING
  (SELECT extractValue(value(t),'//@SELECTION_ID') SELECTION_ID,
    extractValue(value(t),'//@ITEM_ID') ITEM_ID,
    extractValue(value(t),'//@ITEM_ROLE') ITEM_ROLE
  FROM TABLE(XMLSequence(XMLType(xrow) .extract('//EL'))) t
  ) imp_t ON (cur_t.SELECTION_ID=imp_t.SELECTION_ID AND cur_t.ITEM_ID=imp_t.ITEM_ID AND cur_t.ITEM_ROLE=imp_t.ITEM_ROLE)
WHEN NOT matched THEN
  INSERT
    (
      cur_t. SELECTION_ID ,
      cur_t. ITEM_ID ,
      cur_t. ITEM_ROLE
    )
    VALUES
    (
      imp_t. SELECTION_ID ,
      imp_t. ITEM_ID ,
      imp_t. ITEM_ROLE
    );
END;
/
BEGIN
  export_selections_items();
  import_selections_items();
END;
/
--selections_barchart
SELECT * FROM selections_barchart;
CREATE OR REPLACE PROCEDURE export_selections_barchart
IS
  file1 utl_file.file_type;
  xrow CLOB;
BEGIN
  SELECT XMLELEMENT(root,XMLAGG(XMLELEMENT(el, XMLATTRIBUTES( e.selection_id, e.BARCHART_ID ) ))).getCLOBVal() AS xm
  INTO xrow
  FROM selections_barchart e;
  DBMS_XSLPROCESSOR.CLOB2FILE( xrow, 'EXPORTFILE', 'selections_barchart.TXT');
END;
/
CREATE OR REPLACE PROCEDURE import_selections_barchart
IS
  xrow CLOB;
BEGIN
  xrow := DBMS_XSLPROCESSOR.READ2CLOB( 'EXPORTFILE', 'selections_barchart.TXT',0);
  merge INTO selections_barchart cur_t USING
  (SELECT extractValue(value(t),'//@SELECTION_ID') SELECTION_ID,
    extractValue(value(t),'//@BARCHART_ID') BARCHART_ID
  FROM TABLE(XMLSequence(XMLType(xrow) .extract('//EL'))) t
  ) imp_t ON (cur_t.SELECTION_ID=imp_t.SELECTION_ID AND cur_t.BARCHART_ID=imp_t.BARCHART_ID)
WHEN NOT matched THEN
  INSERT
    (
      cur_t. SELECTION_ID ,
      cur_t. BARCHART_ID
    )
    VALUES
    (
      imp_t. SELECTION_ID ,
      imp_t. BARCHART_ID
    );
END;
/
BEGIN
  --export_selections_barchart();
  import_selections_barchart();
END;
--selections_bubblechart
SELECT * FROM selections_bubblechart;
/
CREATE OR REPLACE PROCEDURE export_selections_bubblechart
IS
  file1 utl_file.file_type;
  xrow CLOB;
BEGIN
  SELECT XMLELEMENT(root,XMLAGG(XMLELEMENT(el, XMLATTRIBUTES( e.selection_id, e.BUBBLECHART_ID ) ))).getCLOBVal() AS xm
  INTO xrow
  FROM selections_bubblechart e;
  DBMS_XSLPROCESSOR.CLOB2FILE( xrow, 'EXPORTFILE', 'selections_bubblechart.TXT');
END;
/
CREATE OR REPLACE PROCEDURE import_selections_bubblechart
IS
  xrow CLOB;
BEGIN
  xrow := DBMS_XSLPROCESSOR.READ2CLOB( 'EXPORTFILE', 'selections_bubblechart.TXT',0);
  merge INTO selections_bubblechart cur_t USING
  (SELECT extractValue(value(t),'//@SELECTION_ID') SELECTION_ID,
    extractValue(value(t),'//@BUBBLECHART_ID') BUBBLECHART_ID
  FROM TABLE(XMLSequence(XMLType(xrow) .extract('//EL'))) t
  ) imp_t ON (cur_t.SELECTION_ID=imp_t.SELECTION_ID AND cur_t.BUBBLECHART_ID=imp_t.BUBBLECHART_ID)
WHEN NOT matched THEN
  INSERT
    (
      cur_t. SELECTION_ID ,
      cur_t. BUBBLECHART_ID
    )
    VALUES
    (
      imp_t. SELECTION_ID ,
      imp_t. BUBBLECHART_ID
    );
END;
/
BEGIN
  export_selections_bubblechart();
  import_selections_bubblechart();
END;
--barchart_labels
/
SELECT * FROM barchart_labels;
/
CREATE OR REPLACE PROCEDURE export_barchart_labels
IS
  file1 utl_file.file_type;
  xrow CLOB;
BEGIN
  SELECT XMLELEMENT(root,XMLAGG(XMLELEMENT(el, XMLATTRIBUTES( e.BARCHART_ID, e.LABEL ) ))).getCLOBVal() AS xm
  INTO xrow
  FROM barchart_labels e;
  DBMS_XSLPROCESSOR.CLOB2FILE( xrow, 'EXPORTFILE', 'barchart_labels.TXT');
END;
/
CREATE OR REPLACE PROCEDURE import_barchart_labels
IS
  xrow CLOB;
BEGIN
  xrow := DBMS_XSLPROCESSOR.READ2CLOB( 'EXPORTFILE', 'barchart_labels.TXT',0);
  merge INTO barchart_labels cur_t USING
  (SELECT extractValue(value(t),'//@BARCHART_ID') BARCHART_ID,
    extractValue(value(t),'//@LABEL') LABEL
  FROM TABLE(XMLSequence(XMLType(xrow) .extract('//EL'))) t
  ) imp_t ON (cur_t.BARCHART_ID=imp_t.BARCHART_ID)
WHEN NOT matched THEN
  INSERT
    (
      cur_t. BARCHART_ID ,
      cur_t. LABEL
    )
    VALUES
    (
      imp_t. BARCHART_ID ,
      imp_t. LABEL
    );
END;
/
BEGIN
  export_barchart_labels();
  import_barchart_labels();
END;
--barchart
/
SELECT * FROM barchart;
/
CREATE OR REPLACE PROCEDURE export_barchart
IS
  file1 utl_file.file_type;
  xrow CLOB;
BEGIN
  SELECT XMLELEMENT(root,XMLAGG(XMLELEMENT(el, XMLATTRIBUTES( e.ID, e.MINCOUNTOFMATCHES, e.NAME, e.XAXIS, e.YAXIS, e.USER_ID ) ))).getCLOBVal() AS xm
  INTO xrow
  FROM barchart e;
  DBMS_XSLPROCESSOR.CLOB2FILE( xrow, 'EXPORTFILE', 'barchart.TXT');
END;
/
CREATE OR REPLACE PROCEDURE import_barchart
IS
  xrow CLOB;
BEGIN
  xrow := DBMS_XSLPROCESSOR.READ2CLOB( 'EXPORTFILE', 'barchart.TXT',0);
  merge INTO barchart cur_t USING
  (SELECT extractValue(value(t),'//@ ID ') ID ,
    extractValue(value(t),'//@ MINCOUNTOFMATCHES ') MINCOUNTOFMATCHES ,
    extractValue(value(t),'//@ NAME ') NAME ,
    extractValue(value(t),'//@ XAXIS ') XAXIS ,
    extractValue(value(t),'//@ YAXIS ') YAXIS ,
    extractValue(value(t),'//@ USER_ID ') USER_ID
  FROM TABLE(XMLSequence(XMLType(xrow) .extract('//EL'))) t
  ) imp_t ON (cur_t.id=imp_t.id)
WHEN NOT matched THEN
  INSERT
    (
      cur_t. ID ,
      cur_t. MINCOUNTOFMATCHES ,
      cur_t. NAME ,
      cur_t. XAXIS ,
      cur_t. YAXIS ,
      cur_t. USER_ID
    )
    VALUES
    (
      imp_t. ID ,
      imp_t. MINCOUNTOFMATCHES ,
      imp_t. NAME ,
      imp_t. XAXIS ,
      imp_t. YAXIS ,
      imp_t. USER_ID
    );
END;
/
BEGIN
  export_barchart();
  import_barchart();
END;
--BUBBLECHART
/
SELECT * FROM BUBBLECHART;
/
CREATE OR REPLACE PROCEDURE export_BUBBLECHART
IS
  file1 utl_file.file_type;
  xrow CLOB;
BEGIN
  SELECT XMLELEMENT(root,XMLAGG(XMLELEMENT(el, XMLATTRIBUTES( e. ID , e. MINCOUNTOFMATCHES , e. NAME , e. XAXIS , e. YAXIS , e. XSCALE , e. USER_ID ) ))).getCLOBVal() AS xm
  INTO xrow
  FROM BUBBLECHART e;
  DBMS_XSLPROCESSOR.CLOB2FILE( xrow, 'EXPORTFILE', 'BUBBLECHART.TXT');
END;
/
CREATE OR REPLACE PROCEDURE import_BUBBLECHART
IS
  xrow CLOB;
BEGIN
  xrow := DBMS_XSLPROCESSOR.READ2CLOB( 'EXPORTFILE', 'BUBBLECHART.TXT',0);
  merge INTO BUBBLECHART cur_t USING
  (SELECT extractValue(value(t),'//@ ID ') ID ,
    extractValue(value(t),'//@ MINCOUNTOFMATCHES ') MINCOUNTOFMATCHES ,
    extractValue(value(t),'//@ NAME ') NAME ,
    extractValue(value(t),'//@ XAXIS ') XAXIS ,
    extractValue(value(t),'//@ YAXIS ') YAXIS ,
    extractValue(value(t),'//@ XSCALE ') XSCALE ,
    extractValue(value(t),'//@ USER_ID ') USER_ID
  FROM TABLE(XMLSequence(XMLType(xrow) .extract('//EL'))) t
  ) imp_t ON (cur_t.id=imp_t.id)
WHEN NOT matched THEN
  INSERT
    (
      cur_t. ID ,
      cur_t. MINCOUNTOFMATCHES ,
      cur_t. NAME ,
      cur_t. XAXIS ,
      cur_t. YAXIS ,
      cur_t. XSCALE ,
      cur_t. USER_ID
    )
    VALUES
    (
      imp_t. ID ,
      imp_t. MINCOUNTOFMATCHES ,
      imp_t. NAME ,
      imp_t. XAXIS ,
      imp_t. YAXIS ,
      imp_t. XSCALE ,
      imp_t. USER_ID
    );
END;
/
BEGIN
  export_BUBBLECHART();
  import_BUBBLECHART();
END;
--LINECHART
/
SELECT * FROM LINECHART;
/
CREATE OR REPLACE PROCEDURE export_LINECHART
IS
  file1 utl_file.file_type;
  xrow CLOB;
BEGIN
  SELECT XMLELEMENT(root,XMLAGG(XMLELEMENT(el, XMLATTRIBUTES( e. ID , e. MINCOUNTOFMATCHES , e. NAME , e. XAXIS , e. YAXIS , e. COUNTOFLABELS , e. ISDESC , e. SELECTIONID , e. USER_ID ) ))).getCLOBVal() AS xm
  INTO xrow
  FROM LINECHART e;
  DBMS_XSLPROCESSOR.CLOB2FILE( xrow, 'EXPORTFILE', 'LINECHART.TXT');
END;
/
CREATE OR REPLACE PROCEDURE import_LINECHART
IS
  xrow CLOB;
BEGIN
  xrow := DBMS_XSLPROCESSOR.READ2CLOB( 'EXPORTFILE', 'LINECHART.TXT',0);
  merge INTO LINECHART cur_t USING
  (SELECT extractValue(value(t),'//@ ID ') ID ,
    extractValue(value(t),'//@ MINCOUNTOFMATCHES ') MINCOUNTOFMATCHES ,
    extractValue(value(t),'//@ NAME ') NAME ,
    extractValue(value(t),'//@ XAXIS ') XAXIS ,
    extractValue(value(t),'//@ YAXIS ') YAXIS ,
    extractValue(value(t),'//@ COUNTOFLABELS ') COUNTOFLABELS ,
    extractValue(value(t),'//@ ISDESC ') ISDESC ,
    extractValue(value(t),'//@ SELECTIONID ') SELECTIONID ,
    extractValue(value(t),'//@ USER_ID ') USER_ID
  FROM TABLE(XMLSequence(XMLType(xrow) .extract('//EL'))) t
  ) imp_t ON (cur_t.id=imp_t.id)
WHEN NOT matched THEN
  INSERT
    (
      cur_t. ID ,
      cur_t. MINCOUNTOFMATCHES ,
      cur_t. NAME ,
      cur_t. XAXIS ,
      cur_t. YAXIS ,
      cur_t. COUNTOFLABELS ,
      cur_t. ISDESC ,
      cur_t. SELECTIONID ,
      cur_t. USER_ID
    )
    VALUES
    (
      imp_t. ID ,
      imp_t. MINCOUNTOFMATCHES ,
      imp_t. NAME ,
      imp_t. XAXIS ,
      imp_t. YAXIS ,
      imp_t. COUNTOFLABELS ,
      imp_t. ISDESC ,
      imp_t. SELECTIONID ,
      imp_t. USER_ID
    );
END;
/
BEGIN
  export_LINECHART();
  import_LINECHART();
END;


--general
/
create or replace procedure exportApp
is
begin
  export_USERS();
  export_selections();
  export_selections_heroes();
  export_selections_items();
  export_selections_barchart();
  export_selections_bubblechart();
  export_barchart();
  export_linechart();
  export_bubblechart();
  export_barchart_labels();
end;

/
create or replace procedure importApp
is
begin
  import_USERS();
  import_selections();
  import_selections_heroes();
  import_selections_items();
  import_selections_barchart();
  import_selections_bubblechart();
  import_barchart();
  import_linechart();
  import_bubblechart();
  import_barchart_labels();
end;
/
begin
  exportApp();
  importApp();
end;