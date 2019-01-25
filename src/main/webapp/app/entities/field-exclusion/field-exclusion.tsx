import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './field-exclusion.reducer';
import { IFieldExclusion } from 'app/shared/model/field-exclusion.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFieldExclusionProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class FieldExclusion extends React.Component<IFieldExclusionProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { fieldExclusionList, match } = this.props;
    return (
      <div>
        <h2 id="field-exclusion-heading">
          <Translate contentKey="serviceNetApp.fieldExclusion.home.title">Field Exclusions</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.fieldExclusion.home.createLabel">Create new Field Exclusion</Translate>
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
                  <Translate contentKey="serviceNetApp.fieldExclusion.fields">Fields</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.fieldExclusion.entity">Entity</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {fieldExclusionList.map((fieldExclusion, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${fieldExclusion.id}`} color="link" size="sm">
                      {fieldExclusion.id}
                    </Button>
                  </td>
                  <td>{fieldExclusion.fields}</td>
                  <td>{fieldExclusion.entity}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${fieldExclusion.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${fieldExclusion.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${fieldExclusion.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ fieldExclusion }: IRootState) => ({
  fieldExclusionList: fieldExclusion.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FieldExclusion);
