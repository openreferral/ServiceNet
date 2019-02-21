import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './data-import-report.reducer';
import { IDataImportReport } from 'app/shared/model/data-import-report.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDataImportReportProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class DataImportReport extends React.Component<IDataImportReportProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { dataImportReportList, match } = this.props;
    return (
      <div>
        <h2 id="data-import-report-heading">
          <Translate contentKey="serviceNetApp.dataImportReport.home.title">Data Import Reports</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.dataImportReport.home.createLabel">Create new Data Import Report</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.dataImportReport.numberOfUpdatedServices">Number Of Updated Services</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.dataImportReport.numberOfCreatedServices">Number Of Created Services</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.dataImportReport.numberOfUpdatedOrgs">Number Of Updated Orgs</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.dataImportReport.numberOfCreatedOrgs">Number Of Created Orgs</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.dataImportReport.startDate">Start Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.dataImportReport.endDate">End Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.dataImportReport.jobName">Job Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.dataImportReport.errorMessage" />
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.dataImportReport.documentUpload">Document Upload</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {dataImportReportList.map((dataImportReport, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${dataImportReport.id}`} color="link" size="sm">
                      {dataImportReport.id}
                    </Button>
                  </td>
                  <td>{dataImportReport.numberOfUpdatedServices}</td>
                  <td>{dataImportReport.numberOfCreatedServices}</td>
                  <td>{dataImportReport.numberOfUpdatedOrgs}</td>
                  <td>{dataImportReport.numberOfCreatedOrgs}</td>
                  <td>
                    <TextFormat type="date" value={dataImportReport.startDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={dataImportReport.endDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{dataImportReport.jobName}</td>
                  <td>{dataImportReport.errorMessage}</td>
                  <td>
                    {dataImportReport.documentUploadId ? (
                      <Link to={`document-upload/${dataImportReport.documentUploadId}`}>{dataImportReport.documentUploadId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${dataImportReport.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${dataImportReport.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${dataImportReport.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ dataImportReport }: IRootState) => ({
  dataImportReportList: dataImportReport.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DataImportReport);
