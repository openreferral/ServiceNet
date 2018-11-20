import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './language.reducer';
import { ILanguage } from 'app/shared/model/language.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILanguageProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Language extends React.Component<ILanguageProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { languageList, match } = this.props;
    return (
      <div>
        <h2 id="language-heading">
          <Translate contentKey="serviceNetApp.language.home.title">Languages</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.language.home.createLabel">Create new Language</Translate>
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
                  <Translate contentKey="serviceNetApp.language.language">Language</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.language.srvc">Srvc</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.language.location">Location</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {languageList.map((language, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${language.id}`} color="link" size="sm">
                      {language.id}
                    </Button>
                  </td>
                  <td>{language.language}</td>
                  <td>{language.srvcName ? <Link to={`service/${language.srvcId}`}>{language.srvcName}</Link> : ''}</td>
                  <td>{language.locationName ? <Link to={`location/${language.locationId}`}>{language.locationName}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${language.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${language.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${language.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ language }: IRootState) => ({
  languageList: language.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Language);
