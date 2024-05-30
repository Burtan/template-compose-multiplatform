import sqlite3InitModule from "@sqlite.org/sqlite-wasm";

let db = null;
let sqlModuleReady = null;

self.onmessage = (event) => {
    if (sqlModuleReady == null) {
        sqlModuleReady = createDatabase(event.data.name);
    } else {
        return sqlModuleReady
            .then(handleMessage.bind(event))
            .catch(handleError.bind(event));
    }
}

async function createDatabase(name) {
    const sqlite3 = await sqlite3InitModule()

    if ("opfs" in sqlite3) {
        db = new sqlite3.oo1.OpfsDb(name);
        console.log("OPFS is available, created persisted database at", db.filename);
    } else {
        db = new sqlite3.oo1.DB(name, "ct");
        console.log("OPFS is not available, created transient database", db.filename);
  }
}

function handleMessage() {
    const data = this.data;

    switch (data && data.action) {
        case "exec":
            if (!data["sql"]) {
                throw new Error("exec: Missing query string");
            }

            return postMessage({
                id: data.id,
                results: { values: db.exec({ sql: data.sql, bind: data.params, returnValue: "resultRows" }) },
            })
        case "begin_transaction":
            return postMessage({
                id: data.id,
                results: db.exec("BEGIN TRANSACTION;"),
            })
        case "end_transaction":
            return postMessage({
                id: data.id,
                results: db.exec("END TRANSACTION;"),
            })
        case "rollback_transaction":
            return postMessage({
                id: data.id,
                results: db.exec("ROLLBACK TRANSACTION;"),
            })
        default:
            throw new Error(`Unsupported action: ${data && data.action}`);
    }
}

function handleError(err) {
    return postMessage({
        id: this.data.id,
        error: err,
    });
}
